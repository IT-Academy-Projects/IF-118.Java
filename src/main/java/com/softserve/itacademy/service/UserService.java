package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.UserDto;

import java.util.List;


public interface UserService {
    List<UserDto> findAll();
    void updateDisabled(Integer id, Boolean disabled);
}
