package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.s3.S3Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import static com.softserve.itacademy.service.s3.S3Constants.MATERIALS_FOLDER;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final CourseService courseService;
    private final MaterialConverter materialConverter;
    private final S3Utils s3Utils;

    public MaterialServiceImpl(MaterialRepository materialRepository,
                               MaterialConverter materialConverter,
                               CourseService courseService,
                               S3Utils s3Utils) {
        this.materialRepository = materialRepository;
        this.materialConverter = materialConverter;
        this.courseService = courseService;
        this.s3Utils = s3Utils;
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
                .fileReference(s3Utils.saveFile(file, BUCKET_NAME, MATERIALS_FOLDER))
                .build();
        material = materialRepository.save(material);

        return materialConverter.of(material);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        Material material = getById(id);
        String extension = s3Utils.getFileExtension(material.getFileReference());
        return DownloadFileResponse.builder()
                .file(s3Utils.downloadFile(material.getFileReference(), BUCKET_NAME, MATERIALS_FOLDER))
                .fileName(material.getName() + "." + extension)
                .build();
    }

    @Override
    public void delete(Integer id, Integer currentUserId) {
        Material material = getById(id);
        if (!material.getOwnerId().equals(currentUserId)) {
            throw new OperationNotAllowedException("You are not owner of this course");
        }
        s3Utils.delete(BUCKET_NAME, MATERIALS_FOLDER + "/" + material.getFileReference());
        materialRepository.delete(material);
    }

    @Override
    public Material getById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material with such id was not found"));
    }
}
