package com.softserve.itacademy.security.service;

import com.softserve.itacademy.entity.User;

public interface UserPrincipalService {
    User getByEmail(String email);
}
