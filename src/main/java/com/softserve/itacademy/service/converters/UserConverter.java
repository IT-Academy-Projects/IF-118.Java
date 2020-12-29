package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.UserRequest;
import com.softserve.itacademy.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserConverter {

    private final ModelMapper mapper;

    public UserResponse convertToDto(User user) {
        UserResponse map = mapper.map(user, UserResponse.class);
        map.setDisabled(user.getDisabled());
        return map;
    }

    public User convertToUser(UserRequest userRequest) {
        User map = mapper.map(userRequest, User.class);
        map.setDisabled(false);
        return map;

    }
}
