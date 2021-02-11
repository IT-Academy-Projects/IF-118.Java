package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.OperationNotAllowedException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.implementation.MaterialServiceImpl;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

@ExtendWith(SpringExtension.class)
public class MaterialServiceImplTest {
    @InjectMocks
    private MaterialServiceImpl materialService;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private CourseService courseService;
    @Mock
    private AmazonS3ClientService amazonS3ClientService;
    @Mock
    private MaterialConverter materialConverter;

    @Test
    void createMaterialWithFileTest() throws IOException {

        Path path = Paths.get("src/test/resources/text.txt");
        MultipartFile file = new MockMultipartFile("text.txt",
                "text.txt", "text/plain", Files.readAllBytes(path));
        Material material = buildMaterial();
        when(courseService.getById(anyInt())).thenReturn(courseBuild());
        when(materialRepository.save(any())).thenReturn(material);
        MaterialRequest materialRequest = buildRequest();
        materialService.create(materialRequest, file);
        verify(materialRepository, times(1)).save(material);
        verify(amazonS3ClientService, times(1)).upload(anyString(), anyString(), eq(file));
    }

    @Test
    void createMaterialWithoutFileTest() {
        Material material = buildMaterial();
        when(courseService.getById(anyInt())).thenReturn(courseBuild());
        when(materialRepository.save(any())).thenReturn(material);
        materialService.create(buildRequest(), null);
        verify(materialRepository, times(1)).save(material);
    }

    @Test
    void courseIsDisabledTest() {
        Material material = buildMaterial();
        Course course = courseBuild();
        course.setDisabled(true);
        when(courseService.getById(anyInt())).thenReturn(course);
        when(materialRepository.save(any())).thenReturn(material);
        assertThrows(DisabledObjectException.class, () -> materialService.create(buildRequest(), null));
    }

    @Test
    void downloadTest() {
        Material material = buildMaterial();
        when(materialRepository.findById(anyInt())).thenReturn(java.util.Optional.of(material));
        materialService.downloadById(1);
        verify(amazonS3ClientService, times(1)).download(anyString(), eq(material.getFileReference()));
    }

    @Test
    void deleteSuccessfulTest() {
        Material material = buildMaterial();
        when(materialRepository.findById(anyInt())).thenReturn(java.util.Optional.of(material));
        materialService.delete(1, 1);
        verify(amazonS3ClientService, times(1)).delete(anyString(), anyString(), eq(material.getFileReference()));
        verify(materialRepository, times(1)).delete(material);
    }

    @Test
    void deleteNotOwnerExceptionTest() {
        Material material = buildMaterial();
        when(materialRepository.findById(anyInt())).thenReturn(java.util.Optional.of(material));

        assertThrows(OperationNotAllowedException.class, () ->  materialService.delete(1, 2));
    }

    @Test
    void deleteNotFoundException() {
        assertThrows(NotFoundException.class, () -> materialService.delete(1, 2));
    }

    private Material buildMaterial() {
        return Material.builder()
                .name("material")
                .description("description")
                .ownerId(1)
                .fileReference("reference")
                .build();
    }

    private MaterialRequest buildRequest() {
        return MaterialRequest.builder()
                .name("material")
                .description("description")
                .ownerId(1)
                .courseId(1)
                .build();
    }

    private Course courseBuild() {
        return Course.builder()
                .name("course")
                .ownerId(1)
                .description("course description")
                .disabled(false)
                .groups(Collections.emptySet())
                .build();
    }
}