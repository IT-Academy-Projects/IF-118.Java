package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;


@Getter
@Setter
public class UserDto {

    private Long id;
    private User.AccountType accountType;
    private String email;
    private String password;
    private String name;
    private Boolean disabled;
    private Boolean isAdmin;

    private UserDto() {};

    public static UserDto create(User user) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.accountType = user.getAccountType();
        userDto.email = user.getEmail();
        userDto.password = user.getPassword();
        userDto.name = user.getName();
        userDto.disabled = user.getDisabled();
        userDto.isAdmin = user.getIsAdmin();

        return userDto;
    }

}
