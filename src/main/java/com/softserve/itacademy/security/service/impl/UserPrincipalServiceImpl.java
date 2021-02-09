package com.softserve.itacademy.security.service.impl;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.service.UserPrincipalService;
import org.springframework.stereotype.Service;

import static com.softserve.itacademy.config.Constance.USER_EMAIL_NOT_FOUND;

@Service
public class UserPrincipalServiceImpl implements UserPrincipalService {

    private final UserRepository userRepository;

    public UserPrincipalServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(USER_EMAIL_NOT_FOUND));
    }
}
