package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.security.Role;

public interface RoleService {
    Role findByNameIgnoreCase(String name);
}
