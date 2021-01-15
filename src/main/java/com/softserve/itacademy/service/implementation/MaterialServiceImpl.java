package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.FileHasNoExtensionException;
import com.softserve.itacademy.exception.FileProcessingException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import static com.softserve.itacademy.service.s3.S3Constants.MATERIALS_FOLDER;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;
    private CourseService courseService;
    private MaterialConverter materialConverter;
    private AmazonS3ClientService amazonS3ClientService;

    public MaterialServiceImpl(MaterialRepository materialRepository,
                               MaterialConverter materialConverter,
                               CourseService courseService,
                               AmazonS3ClientService amazonS3ClientService) {
        this.materialRepository = materialRepository;
        this.materialConverter = materialConverter;
        this.courseService = courseService;
        this.amazonS3ClientService = amazonS3ClientService;
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
                .fileReference(saveFile(file))
                .build();
        material = materialRepository.save(material);

        return materialConverter.of(material);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        Material material = getById(id);
        String[] split = material.getFileReference().split("\\.");
        if (split.length < 1) {
            throw new FileHasNoExtensionException("Wrong file format");
        }
        String extension = split[split.length - 1];
        return DownloadFileResponse.builder()
                .file(downloadFile(material.getFileReference()))
                .fileName(material.getName() + "." + extension)
                .build();
    }

    @Override
    public void delete(Integer id, Integer currentUserId) {
        Material material = getById(id);
        if (!material.getOwnerId().equals(currentUserId)) { throw new OperationNotAllowedException("You are not owner of this course"); }
        amazonS3ClientService.delete(BUCKET_NAME, MATERIALS_FOLDER + "/" + material.getFileReference());
        materialRepository.delete(material);
    }

    @Override
    public Material getById(Integer id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Material with such id was not found"));
    }

    private String saveFile(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        if (split.length < 1) {
            throw new FileHasNoExtensionException("Wrong file format");
        }
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;
        try {
            amazonS3ClientService.upload(BUCKET_NAME, MATERIALS_FOLDER + "/" + fileReference, file.getInputStream());
        } catch (IOException e) {
            throw new FileProcessingException("Cannot write file");
        }
        return fileReference;
    }

    private byte[] downloadFile(String fileReference) {
        byte[] bytes = new byte[0];
        try {
            bytes = amazonS3ClientService.download(BUCKET_NAME, MATERIALS_FOLDER + "/" + fileReference);
        } catch (IOException e) {
            throw new FileProcessingException("Cannot read file");
        }
        return bytes;
    }

}
