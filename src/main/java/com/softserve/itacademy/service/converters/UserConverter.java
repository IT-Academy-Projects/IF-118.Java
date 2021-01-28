package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.response.UserStatisticResponse;
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
        map.setAvatar(user.getAvatar());
        return map;
    }

    public UserStatisticResponse statisticOf(User user) {
        return mapper.map(user, UserStatisticResponse.class);
    }
}
