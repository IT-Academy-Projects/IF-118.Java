package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.ImageRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.converters.CourseConverter;
import com.softserve.itacademy.service.implementation.CourseServiceImpl;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Set;

public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private MaterialRepository materialRepository;
    @Mock
    private UserService userService;
    @Mock
    private CourseConverter courseConverter;
    @Mock
    private ImageService imageService;
    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @BeforeEach
    public void setupBeforeClass() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCourseWithEmptyMaterialsFile() {

        when(courseRepository.save(any(Course.class))).thenReturn(generateCourse());
        when(courseConverter.of(any(Course.class))).thenReturn(generateCourseResponse());

        CourseResponse courseResponse = courseServiceImpl.create(generateCourseRequest(), null);

        assertEquals("NewCourse", courseResponse.getName());
        assertEquals(1, courseResponse.getOwnerId());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void createCourseWithMaterials() {
        when(materialRepository.findByIds(anySet())).thenReturn(Set.of(generateMaterial()));
        Course course = generateCourse();
        course.setMaterials(Set.of(generateMaterial()));
        when(courseConverter.of(any(CourseRequest.class), anySet())).thenReturn(course);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        CourseResponse courseResponse = generateCourseResponse();
        courseResponse.setMaterialIds(Set.of(1));
        when(courseConverter.of(any(Course.class))).thenReturn(courseResponse);

        CourseResponse result = courseServiceImpl.create(generateCourseRequest(), null);

        assertEquals(Set.of(1), courseResponse.getMaterialIds());
    }

    @Test
    public void create_whenInvalidOwnerId_thenThrowsNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseRequest(), null));
    }

//    @Test
//    public void create_whenInvalidGroupId_thenThrowsNotFoundException() {
//        when(groupService.findById(anyInt())).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseDto(), null));
//    }

    private CourseResponse generateCourseResponse() {
        return CourseResponse.builder()
                .name("NewCourse")
                .ownerId(1)
                .groupIds(Collections.emptySet())
                .materialIds(Collections.emptySet())
                .disabled(false)
                .description("Description")
                .imageId(1)
                .build();
    }
    private CourseRequest generateCourseRequest() {
        return CourseRequest.builder()
                .name("NewCourse")
                .description("Description")
                .ownerId(1)
                .materialIds(Collections.emptySet())
                .build();
    }

    private Course generateCourse() {
        return Course.builder()
                .name("NewCourse")
                .ownerId(1)
                .disabled(false)
                .description("Description")
                .groups(Collections.emptySet())
                .materials(Collections.emptySet())
                .build();
    }

    private Material generateMaterial () {
        return Material.builder()
                .name("Material")
                .ownerId(1)
                .build();
    }
}
