package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.UserRequest;
import com.softserve.itacademy.response.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public UserResponse convertToDto(User user) {
        return UserResponse
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .disabled(user.getDisabled())
                .build();
    }

    public User convertToUser(UserRequest userRequest) {
        return User.builder()
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .disabled(false)
                .build();

    }
}
