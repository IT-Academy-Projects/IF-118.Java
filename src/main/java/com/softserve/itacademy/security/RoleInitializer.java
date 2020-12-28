package com.softserve.itacademy.security;

import com.softserve.itacademy.entity.security.Authority;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.repository.security.AuthorityRepository;
import com.softserve.itacademy.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class RoleInitializer implements CommandLineRunner {

    private AuthorityRepository authorityRepository;

    private RoleRepository roleRepository;

    @Autowired
    public RoleInitializer(AuthorityRepository authorityRepository, RoleRepository roleRepository) {
        this.authorityRepository = authorityRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//        loadSecurityData();
    }

    //TODO: Rework default roles
    private void loadSecurityData() {

        roleRepository.deleteAll();
        authorityRepository.deleteAll();

        Authority updateUser = authorityRepository.save(Authority.builder().name("user.update").build());
        Authority deleteUser = authorityRepository.save(Authority.builder().name("user.delete").build());


        Authority createGroup = authorityRepository.save(Authority.builder().name("group.create").build());
        Authority readGroup = authorityRepository.save(Authority.builder().name("group.read").build());
        Authority updateGroup = authorityRepository.save(Authority.builder().name("group.update").build());
        Authority deleteGroup = authorityRepository.save(Authority.builder().name("group.delete").build());


        Authority createCourse = authorityRepository.save(Authority.builder().name("course.create").build());
        Authority readCourse = authorityRepository.save(Authority.builder().name("course.read").build());
        Authority updateCourse = authorityRepository.save(Authority.builder().name("course.update").build());
        Authority deleteCourse = authorityRepository.save(Authority.builder().name("course.delete").build());


        Role userRole = roleRepository.save(Role.builder().name("USER").build());
        Role adminRole = roleRepository.save(Role.builder().name("ADMIN").build());
        Role studentRole = roleRepository.save(Role.builder().name("STUDENT").build());
        Role teacherRole = roleRepository.save(Role.builder().name("TEACHER").build());


        adminRole.setAuthorities(new HashSet<>(authorityRepository.findAll()));

        studentRole.setAuthorities(new HashSet<>(Set.of(readCourse, readGroup)));

        teacherRole.setAuthorities(new HashSet<>(Set.of(
                createCourse, updateCourse, deleteCourse,
                createGroup, updateGroup, deleteGroup
        )));

        roleRepository.saveAll(List.of(userRole, adminRole, studentRole, teacherRole));

    }


}
