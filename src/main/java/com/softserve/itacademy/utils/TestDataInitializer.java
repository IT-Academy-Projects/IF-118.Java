package com.softserve.itacademy.utils;


import com.softserve.itacademy.entity.*;
import com.softserve.itacademy.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class TestDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

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
        self.addStudentToGroup(1L, 5L);
    }

    private void insertTestData() {


        Teacher firstTeacher = new Teacher();
        firstTeacher.setEmail("teacher1@gmail.com");
        firstTeacher.setPassword("password");
        firstTeacher.setName("Teacher 1");
        teacherRepository.save(firstTeacher);


        Teacher secondTeacher = new Teacher();
        secondTeacher.setEmail("teacher2@gmail.com");
        secondTeacher.setPassword("password");
        secondTeacher.setName("Teacher 2");
        teacherRepository.save(secondTeacher);

        Course course = courseRepository.save(new Course().setName("Java Course").setTeacher(secondTeacher));


        Group firstGroup = new Group();
        firstGroup.setName("IF-118");
        firstGroup = groupRepository.save(firstGroup);
        firstGroup.setTeacher(secondTeacher);

        Group secondGroup = new Group();
        secondGroup.setName("LV-120");
        secondGroup.getCourses().add(course);
        secondGroup = groupRepository.save(secondGroup);
        secondGroup.setTeacher(secondTeacher);

        Student firstStudent = new Student();
        firstStudent.setEmail("student1@gmail.com");
        firstStudent.setPassword("password");
        firstStudent.setName("Student 1");
        studentRepository.save(firstStudent);
        firstGroup.getStudents().add(firstStudent);
        secondGroup.getStudents().add(firstStudent);

        Student secondStudent = new Student();
        secondStudent.setEmail("student2@gmail.com");
        secondStudent.setPassword("password");
        secondStudent.setName("Student 2");
        studentRepository.save(secondStudent);
        firstGroup.getStudents().add(secondStudent);
        secondGroup.getStudents().add(secondStudent);

        Student thirdStudent = new Student();
        thirdStudent.setEmail("student3@gmail.com");
        thirdStudent.setPassword("password");
        thirdStudent.setName("Student 3");
        studentRepository.save(thirdStudent);

        groupRepository.save(firstGroup);
        groupRepository.save(secondGroup);
    }

    @Transactional
    public void addStudentToGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(RuntimeException::new);
        Student student = studentRepository.findById(studentId).orElseThrow(RuntimeException::new);

         group.getStudents().add(student);
         groupRepository.save(group);

        // TODO: Use this if you want add entity to collection from back reference
        // student.addGroup(group);
        // studentRepository.save(student);
    }


}
