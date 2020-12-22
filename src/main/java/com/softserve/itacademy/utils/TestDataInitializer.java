package com.softserve.itacademy.utils;


import com.softserve.itacademy.entity.*;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TestDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private TestDataInitializer self;

    @PostConstruct
    public void init() {
        log.info("Initializing test data");
        insertTestData();
//        self.addStudentToGroup(1L, 5L);
    }

    private void insertTestData() {


        User firstTeacher = new User();
        firstTeacher.setEmail("teacher1@gmail.com");
        firstTeacher.setPassword("password");
        firstTeacher.setName("Teacher 1");
        userRepository.save(firstTeacher);


        User secondTeacher = new User();
        secondTeacher.setEmail("teacher2@gmail.com");
        secondTeacher.setPassword("password");
        secondTeacher.setName("Teacher 2");
        userRepository.save(secondTeacher);

        Course course = new Course().setName("Java Course").setOwnerId(secondTeacher.getId());
        course.getUsers().add(secondTeacher);

        courseRepository.save(course);


        Group firstGroup = new Group();
        firstGroup.setName("IF-118");
        firstGroup = groupRepository.save(firstGroup);
        firstGroup.getUsers().add(secondTeacher);
        firstGroup.setOwnerId(secondTeacher.getId());

        Group secondGroup = new Group();
        secondGroup.setName("LV-120");
        secondGroup.getCourses().add(course);
        secondGroup = groupRepository.save(secondGroup);
        secondGroup.getUsers().add(secondTeacher);
        secondGroup.setOwnerId(secondTeacher.getId());

        User firstStudent = new User();
        firstStudent.setEmail("student1@gmail.com");
        firstStudent.setPassword("password");
        firstStudent.setName("Student 1");
        userRepository.save(firstStudent);
        firstGroup.getUsers().add(firstStudent);
        secondGroup.getUsers().add(firstStudent);

        User secondStudent = new User();
        secondStudent.setEmail("student2@gmail.com");
        secondStudent.setPassword("password");
        secondStudent.setName("Student 2");
        userRepository.save(secondStudent);
        firstGroup.getUsers().add(secondStudent);
        secondGroup.getUsers().add(secondStudent);

        groupRepository.save(firstGroup);
        groupRepository.save(secondGroup);
    }

//    @Transactional
//    public void addStudentToGroup(Long groupId, Long studentId) {
//        Group group = groupRepository.findById(groupId).orElseThrow(RuntimeException::new);
//        Student student = studentRepository.findById(studentId).orElseThrow(RuntimeException::new);
//
//         group.getStudents().add(student);
//         groupRepository.save(group);
//
//        // TODO: Use this if you want add entity to collection from back reference
//        // student.addGroup(group);
//        // studentRepository.save(student);
//    }


}
