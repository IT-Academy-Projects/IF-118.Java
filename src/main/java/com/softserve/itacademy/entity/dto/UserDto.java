package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDto {

    private Integer id;
    private String email;
    private String password;
    private String name;
    private Boolean disabled;
    private Boolean isAdmin;

    private UserDto() {};

    public static UserDto create(User user) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.email = user.getEmail();
        userDto.password = user.getPassword();
        userDto.name = user.getName();

        return userDto;
    }

}
