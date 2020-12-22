package com.softserve.itacademy.utils;

import com.softserve.itacademy.entity.security.Authority;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.repository.security.AuthorityRepository;
import com.softserve.itacademy.repository.security.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RolesInitializer {

    RoleRepository roleRepository;

    AuthorityRepository authorityRepository;

    @Autowired
    public RolesInitializer(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @PostConstruct
    void init() {
        createRoles();
    }

    void createRoles() {
        Authority createAuthority = authorityRepository.save(Authority.builder().name("createGroups").build());
        Authority joinAuthority = authorityRepository.save(Authority.builder().name("joinGroups").build());

        roleRepository.save(Role.builder().name("Teacher").authority(createAuthority).build());
        roleRepository.save(Role.builder().name("Student").authority(joinAuthority).build());
    }

}
