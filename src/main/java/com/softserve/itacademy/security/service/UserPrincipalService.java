package com.softserve.itacademy.security.service;

import com.softserve.itacademy.entity.User;
import org.springframework.transaction.annotation.Transactional;

public interface UserPrincipalService {
    User getByEmail(String email);
}
