package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.ImageRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.converters.CourseConverter;
import com.softserve.itacademy.service.implementation.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        when(courseConverter.of(any(CourseRequest.class), anySet())).thenReturn(generateCourse());
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
        verify(materialRepository, times(1)).findByIds(anySet());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void testCreateCourseThrowsNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.create(generateCourseRequest(), null));
    }

    @Test
    public void testFindByOwnerId() {
        when(courseRepository.findByOwnerId(anyInt())).thenReturn(List.of(generateCourse()));
        when(courseConverter.of(any(Course.class))).thenReturn(generateCourseResponse());

        List<CourseResponse> result = courseServiceImpl.findByOwner(1);

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getOwnerId());
        verify(courseRepository, times(1)).findByOwnerId(anyInt());
    }

    @Test
    public void testFindByOwnerIdIfNoCourses() {
        when(courseRepository.findByOwnerId(anyInt())).thenReturn(Collections.emptyList());

        List<CourseResponse> result = courseServiceImpl.findByOwner(1);

        assertEquals(0, result.size());
        verify(courseRepository, times(1)).findByOwnerId(anyInt());
    }

    @Test
    public void testUpdateDisabled() {
        when(courseRepository.updateDisabled(anyInt(), anyBoolean())).thenReturn(1);

        courseServiceImpl.updateDisabled(1, true);

        verify(courseRepository, times(1)).updateDisabled(anyInt(), anyBoolean());
    }

    @Test
    public void testUpdateDisabledThrowsNotFoundException() {
        when(courseRepository.updateDisabled(anyInt(), anyBoolean())).thenReturn(0);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.updateDisabled(1, true));
    }

    @Test
    public void testUpdateDescription() {
        when(courseRepository.updateDescription(anyInt(), anyString())).thenReturn(1);

        courseServiceImpl.updateDescription(1, "Some string");

        verify(courseRepository, times(1)).updateDescription(anyInt(), anyString());
    }

    @Test
    public void testUpdateDescriptionThrowsNotFoundException() {
        when(courseRepository.updateDescription(anyInt(), anyString())).thenReturn(0);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.updateDescription(1, "Some string"));
    }

    @Test
    public void testGetAvatar() {
        when(courseRepository.existsById(anyInt())).thenReturn(true);
        byte[] bytes = "string".getBytes();
        when(courseRepository.getAvatarById(anyInt())).thenReturn(bytes);

        byte[] result = courseServiceImpl.getAvatarById(1);

        assertEquals(bytes, result);
        verify(courseRepository, times(1)).getAvatarById(anyInt());
    }

    @Test
    public void testGetAvatarThrowsCourseNotFoundException() {
        when(courseRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.getAvatarById(1));
    }

    @Test
    public void testGetAvatarThrowsAvatarNotFoundException() {
        when(courseRepository.existsById(anyInt())).thenReturn(true);
        when(courseRepository.getAvatarById(anyInt())).thenReturn(null);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.getAvatarById(1));
    }

    @Test
    public void testReadById() {
        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(generateCourse()));
        when(courseConverter.of(any(Course.class))).thenReturn(generateCourseResponse());

        CourseResponse result = courseServiceImpl.readById(1);
        assertEquals("Description", result.getDescription());
        verify(courseRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testReadByIdThrowsNotFoundException() {
        when(courseRepository.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> courseServiceImpl.readById(1));
        verify(courseRepository, times(1)).findById(anyInt());
    }

    @Test
    public void testReadByIdThrowsDisabledObjectException() {
        Course course = generateCourse();
        course.setDisabled(true);
        when(courseRepository.findById(anyInt())).thenReturn(Optional.of(course));

        assertThrows(DisabledObjectException.class, () -> courseServiceImpl.readById(1));
        verify(courseRepository, times(1)).findById(anyInt());
    }


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

    private Material generateMaterial() {
        return Material.builder()
                .name("Material")
                .ownerId(1)
                .build();
    }
}
