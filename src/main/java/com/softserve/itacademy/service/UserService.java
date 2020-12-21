package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.dto.UserDto;

import java.util.List;


public interface UserService {
    List<UserDto> findAll();
}
