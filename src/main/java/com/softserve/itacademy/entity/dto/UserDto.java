package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDto {

    private Integer id;
    private String email;
    private String name;
    private Boolean disabled;

    private UserDto() {};

    public static UserDto create(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.name = user.getName();
        userDto.disabled = user.getDisabled();
        return userDto;
    }

}
