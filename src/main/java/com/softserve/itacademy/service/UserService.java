package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.UserDto;
import com.softserve.itacademy.entity.User;

import java.util.List;


public interface UserService {
    List<UserDto> findAll();
    void updateDisabled(Integer id, Boolean disabled);
    User findById(Integer id);
}
