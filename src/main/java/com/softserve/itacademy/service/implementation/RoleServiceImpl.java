package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.security.RoleRepository;
import com.softserve.itacademy.service.RoleService;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByNameIgnoreCase(String name) {
        return roleRepository.findByNameIgnoreCase(name).orElseThrow(() -> new NotFoundException("Role " + name + " was not found"));
    }

}
