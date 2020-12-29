package com.softserve.itacademy.service;

import com.softserve.itacademy.response.UserResponse;

import java.util.List;


public interface UserService {
    UserResponse findById(Integer id);
    List<UserResponse> findAll();
    void updateDisabled(Integer id, Boolean disabled);
    void updateProfileInfo(Integer id, String name, String email);
}
