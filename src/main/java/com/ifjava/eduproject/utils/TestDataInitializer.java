package com.ifjava.eduproject.utils;

import com.ifjava.eduproject.entity.*;
import com.ifjava.eduproject.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Component
public class TestDataInitializer {

    @Autowired
    private AdminRepository adminRepository;

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
        insertSuperAdmin();
        insertTestData();
        self.addStudentToGroup(1L, 4L);
    }

    private void insertSuperAdmin() {
        Admin admin = new Admin();
        admin.setEmail("admin@gmail.com");
        admin.setPassword("password");
        admin.setName("Admin");
        adminRepository.save(admin);
    }

    private void insertTestData() {
        Course course = courseRepository.save(new Course().setName("Java Course"));

        Group firstGroup = new Group();
        firstGroup.setName("IF-118");
        firstGroup.setCourse(course);
        firstGroup = groupRepository.save(firstGroup);

        Group secondGroup = new Group();
        secondGroup.setName("LV-120");
        secondGroup.setCourse(course);
        secondGroup = groupRepository.save(secondGroup);

        Student firstStudent = new Student();
        firstStudent.setEmail("student1@gmail.com");
        firstStudent.setPassword("password");
        firstStudent.setName("Student 1");
        studentRepository.save(firstStudent);
        firstGroup.getGroupMembers().add(firstStudent);
        secondGroup.getGroupMembers().add(firstStudent);

        Student secondStudent = new Student();
        secondStudent.setEmail("student2@gmail.com");
        secondStudent.setPassword("password");
        secondStudent.setName("Student 2");
        studentRepository.save(secondStudent);
        firstGroup.getGroupMembers().add(secondStudent);
        secondGroup.getGroupMembers().add(secondStudent);

        Student thirdStudent = new Student();
        thirdStudent.setEmail("student3@gmail.com");
        thirdStudent.setPassword("password");
        thirdStudent.setName("Student 3");
        studentRepository.save(thirdStudent);

        Teacher firstGroupTeacher = new Teacher();
        firstGroupTeacher.setEmail("teacher1@gmail.com");
        firstGroupTeacher.setPassword("password");
        firstGroupTeacher.setName("Teacher 1");
        teacherRepository.save(firstGroupTeacher);
        firstGroup.getGroupMembers().add(firstGroupTeacher);

        Teacher secondGroupTeacher = new Teacher();
        secondGroupTeacher.setEmail("teacher2@gmail.com");
        secondGroupTeacher.setPassword("password");
        secondGroupTeacher.setName("Teacher 2");
        teacherRepository.save(secondGroupTeacher);
        secondGroup.getGroupMembers().add(secondGroupTeacher);

        groupRepository.save(firstGroup);
        groupRepository.save(secondGroup);

    }

    @Transactional
    public void addStudentToGroup(Long groupId, Long studentId) {
        Group group = groupRepository.findById(groupId).orElseThrow(RuntimeException::new);
        Student student = studentRepository.findById(studentId).orElseThrow(RuntimeException::new);

         group.getGroupMembers().add(student);
         groupRepository.save(group);

        // TODO: Use this if you want add entity to collection from back reference
        // student.addGroup(group);
        // studentRepository.save(student);
    }


}
