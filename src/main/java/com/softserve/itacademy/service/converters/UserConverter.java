package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.response.UserResponse;
import com.softserve.itacademy.response.statistic.UserFullStatisticResponse;
import com.softserve.itacademy.response.statistic.UserTinyStaticResponse;
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

    public UserFullStatisticResponse statisticOf(User user) {
        return mapper.map(user, UserFullStatisticResponse.class);
    }

    public UserTinyStaticResponse tinyStatisticOf(User user) {
        return mapper.map(user, UserTinyStaticResponse.class);
    }
}
