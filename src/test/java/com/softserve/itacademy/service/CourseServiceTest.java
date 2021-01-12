package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
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
    private GroupService groupService;
    @Mock
    private UserService userService;
    @Mock
    private CourseConverter courseConverter;

    @InjectMocks
    private CourseServiceImpl courseServiceImpl;

    @BeforeEach
    public void setupBeforeClass() throws Exception {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void create_whenValidData_thenCreateCourse() {
        when(courseRepository.save(any(Course.class))).thenReturn(generateCourse());
        when(courseConverter.of(any(), any(), any())).thenReturn(generateCourse());
        when(courseConverter.of(any(Course.class))).thenReturn(generateCourseResponse());

        CourseResponse courseDto = courseServiceImpl.create(generateCourseDto());
        assertEquals("NewCourse", courseDto.getName());
        assertEquals(1, courseDto.getOwnerId());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    public void create_whenInvalidOwnerId_thenThrowsNotFoundException() {
        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseDto()));
    }

    @Test
    public void create_whenInvalidGroupId_thenThrowsNotFoundException() {
        when(groupService.findById(anyInt())).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseDto()));
    }

    private CourseResponse generateCourseResponse() {
        return CourseResponse.builder()
                .name("NewCourse")
                .ownerId(1)
                .groupIds(Set.of(1,2,3))
                .disabled(false)
                .build();
    }
    private CourseRequest generateCourseDto() {
        return CourseRequest.builder()
                .name("NewCourse")
                .ownerId(1)
                .groupIds(Set.of(1,2,3))
                .build();
    }

    private Course generateCourse() {
        return Course.builder()
                .name("NewCourse")
                .ownerId(1)
                .disabled(false)
                .groups(Collections.emptySet())
                .materials(Collections.emptySet())
                .build();
    }
}
