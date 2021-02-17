package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.*;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.repository.EventRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.EventService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import static com.softserve.itacademy.service.s3.S3Constants.MATERIALS_FOLDER;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final CourseService courseService;
    private final MaterialConverter materialConverter;
    private final AmazonS3ClientService amazonS3ClientService;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;

    public MaterialServiceImpl(MaterialRepository materialRepository,
                               MaterialConverter materialConverter,
                               CourseService courseService,
                               AmazonS3ClientService amazonS3ClientService,
                               UserRepository userRepository,
                               EventService eventService,
                               EventRepository eventRepository) {
        this.materialRepository = materialRepository;
        this.materialConverter = materialConverter;
        this.courseService = courseService;
        this.amazonS3ClientService = amazonS3ClientService;
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
    }


    @Override
    public MaterialResponse findById(Integer id) {
        return materialConverter.of(getById(id));
    }

    @Override
    public MaterialResponse create(MaterialRequest materialRequest, MultipartFile file) {
        Course course = courseService.getById(materialRequest.getCourseId());
        if (course.getDisabled()) {
            throw new DisabledObjectException("Object disabled");
        }

        Material material = Material.builder()
                .name(materialRequest.getName())
                .ownerId(materialRequest.getOwnerId())
                .description(materialRequest.getDescription())
                .course(course)
                .groups(new ArrayList<>(course.getGroups()))
                .fileReference(amazonS3ClientService.upload(BUCKET_NAME, MATERIALS_FOLDER, file))
                .build();
        Set<Group> groups = course.getGroups();
        if (groups != null && !groups.isEmpty()) {
            groups.forEach(group -> group.getMaterials().add(material));
        }
        Material savedMaterial = materialRepository.save(material);
        return materialConverter.of(savedMaterial);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        Material material = getById(id);
        String extension = FilenameUtils.getExtension(material.getFileReference());
        return DownloadFileResponse.builder()
                .file(amazonS3ClientService.download(BUCKET_NAME, material.getFileReference()))
                .fileName(material.getName() + "." + extension)
                .build();
    }

    @Override
    public void delete(Integer id, Integer currentUserId) {
        Material material = getById(id);
        if (!material.getOwnerId().equals(currentUserId)) {
            throw new OperationNotAllowedException("You are not owner of this course");
        }
        amazonS3ClientService.delete(BUCKET_NAME, MATERIALS_FOLDER, material.getFileReference());
        materialRepository.delete(material);
    }

    @Transactional
    @Override
    public void open(Integer materialId, List<Integer> groupIds) {
        materialRepository.openMaterial(materialId, groupIds);
        createLectionEvent(materialId, groupIds, Event.EventType.OPEN_LECTION);
    }

    @Transactional
    @Override
    public void closeByExpirationDate(Integer materialId, Integer groupId) {
        materialRepository.closeMaterial(materialId, groupId);
        createLectionEvent(materialId, List.of(groupId), Event.EventType.CLOSE_LECTION);
    }

    @Override
    public Set<MaterialResponse> findAllByCourseId(Integer courseId) {
        Set<Material> materials = materialRepository.findByCourseIds(Set.of(courseId));
        if (materials == null) {
            return Collections.emptySet();
        }
        return materials.stream()
            .map(materialConverter::of)
            .collect(Collectors.toSet());
    }

    @Override
    public Material getById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material with such id was not found"));
    }

    private void createLectionEvent(Integer entityId, List<Integer> groupIds, Event.EventType eventType) {
        Integer creatorId = materialRepository.findOwnerIdById(entityId);
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException("User with id(" + creatorId + ") not found"));

        List<User> recipients = userRepository.findAllByGroupIds(groupIds);

        if (!recipients.isEmpty()) {
            Event event = Event.builder()
                    .creator(creator)
                    .recipients(recipients)
                    .type(eventType)
                    .entityId(entityId)
                    .build();

            eventService.sendNotificationFromEvent(eventRepository.save(event));
        }
    }
}

