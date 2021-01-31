package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.response.UserResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class UserConverter {

    private final ModelMapper mapper;

    public UserResponse of(User user) {
        UserResponse map = mapper.map(user, UserResponse.class);
        map.setDisabled(user.getDisabled());
        if (user.getAvatar() != null) {
            map.setImageId(user.getAvatar().getId());
        }
        return map;
    }
}
