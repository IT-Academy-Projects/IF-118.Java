package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.dto.UserDto;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MySqlUserService implements UserService {

    private UserRepository userRepository;

    @Autowired
    public MySqlUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::create).collect(Collectors.toList());
    }
}
