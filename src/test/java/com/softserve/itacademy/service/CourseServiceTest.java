//package com.softserve.itacademy.service;
//
//import com.softserve.itacademy.entity.Course;
//import com.softserve.itacademy.exception.NotFoundException;
//import com.softserve.itacademy.repository.CourseRepository;
//import com.softserve.itacademy.request.CourseRequest;
//import com.softserve.itacademy.response.CourseResponse;
//import com.softserve.itacademy.service.implementation.CourseServiceImpl;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import static org.mockito.ArgumentMatchers.any;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import static org.mockito.Mockito.anyInt;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//import org.mockito.MockitoAnnotations;
//
//import java.util.Set;
//
//public class CourseServiceTest {
//    @Mock
//    private CourseRepository courseRepository;
//    @Mock
//    private GroupService groupService;
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private CourseServiceImpl courseServiceImpl;
//
//    @BeforeEach
//    public void setupBeforeClass() throws Exception {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void create_whenValidData_thenCreateCourse() {
//        when(courseRepository.save(any(Course.class))).thenReturn(generateCourse());
//
//        CourseResponse courseDto = courseServiceImpl.create(generateCourseDto());
//        assertEquals("NewCourse", courseDto.getName());
//        assertEquals(1, courseDto.getOwnerId());
//        verify(courseRepository, times(1)).save(any(Course.class));
//    }
//
//    @Test
//    public void create_whenInvaildOwnerId_thenThrowsNotFounfExeption() {
//        when(userService.findById(anyInt())).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseDto()));
//    }
//
//    @Test
//    public void create_whenInvaildGroupId_thenThrowsNotFounfExeption() {
//        when(groupService.findById(anyInt())).thenThrow(NotFoundException.class);
//
//        assertThrows(NotFoundException.class, ()-> courseServiceImpl.create(generateCourseDto()));
//    }
//
//    private CourseRequest generateCourseDto() {
//        return CourseRequest.builder()
//                .name("NewCourse")
//                .ownerId(1)
//                .groupIds(Set.of(1,2,3))
//                .build();
//    }
//
//    private Course generateCourse() {
//        Course course = new Course();
//        course.setName("NewCourse");
//        course.setOwnerId(1);
//        return course;
//    }
//}
