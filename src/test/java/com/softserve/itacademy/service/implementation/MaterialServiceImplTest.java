package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.MaterialRequest;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.converters.MaterialConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.apache.commons.io.FilenameUtils;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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

    @Test
    void createMaterialWithFileTest() throws IOException {

        Path path = Paths.get("src/test/resources/test.txt");
        MultipartFile file = new MockMultipartFile("test.txt",
                "test.txt", "text/plain", Files.readAllBytes(path));
        Material material = buildMaterial();
        when(courseService.getById(anyInt())).thenReturn(courseBuild());
        when(materialRepository.save(any())).thenReturn(material);
        materialService.create(buildRequest(), file);
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
        material.setFileReference("reference");
        when(materialRepository.findById(anyInt())).thenReturn(java.util.Optional.of(material));
        materialService.downloadById(1);
        verify(amazonS3ClientService, times(1)).download(anyString(), eq(material.getFileReference()));
        verify(materialRepository, times(1)).delete(material);
    }

    public Material buildMaterial() {
        return Material.builder()
                .name("material")
                .description("description")
                .ownerId(1)
                .build();
    }

    public MaterialRequest buildRequest() {
        return MaterialRequest.builder()
                .name("material")
                .description("description")
                .ownerId(1)
                .courseId(1)
                .build();
    }

    public Course courseBuild() {
        return Course.builder()
                .name("course")
                .ownerId(1)
                .description("course description")
                .disabled(false)
                .groups(Collections.emptySet())
                .build();
    }
}
