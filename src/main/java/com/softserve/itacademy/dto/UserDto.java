package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDto {

    private Integer id;
    private String email;
    private String name;
    private Boolean disabled;

    public static UserDto create(User user) {
        return UserDto
                .builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .disabled(user.getDisabled())
                .build();
    }

}
