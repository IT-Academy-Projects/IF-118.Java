package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.FileHasNoExtension;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import static com.softserve.itacademy.service.s3.S3Util.*;

@Service
public class MaterialServiceImpl implements MaterialService {

    private MaterialRepository materialRepository;
    private CourseService courseService;
    private MaterialConverter materialConverter;
    private AmazonS3ClientService amazonS3ClientService;

    private final String folder = "materials";

    @Autowired
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
        if (course.getDisabled()) { throw new DisabledObjectException(); }

        Material material = Material.builder()
                .name(materialRequest.getName())
                .ownerId(materialRequest.getOwnerId())
                .course(course)
                .fileReference(saveFile(file))
                .build();
        material = materialRepository.save(material);

        return materialConverter.of(material);
    }

    @Override
    public Material getById(Integer id) {
        return materialRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private String saveFile(MultipartFile file) {
        String[] split = file.getOriginalFilename().split("\\.");
        if (split.length < 1) { throw new FileHasNoExtension(); }
        String extension = split[split.length - 1];
        String fileReference = UUID.randomUUID().toString().toLowerCase() + "." + extension;

        File tempFile = new File(TEMPORARY_STORAGE_PATH, fileReference);

        try (OutputStream os = new FileOutputStream(tempFile)) {
            os.write(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        amazonS3ClientService.upload(BUCKET_NAME,  folder + "/" + fileReference, tempFile);
        tempFile.delete();
        return fileReference;
    }

    public byte[] downloadFile(String fileReference) {
        byte[] bytes = new byte[0];
        try {
            bytes = FileUtils.readFileToByteArray(amazonS3ClientService.download(BUCKET_NAME, folder + "/" + fileReference));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

}
