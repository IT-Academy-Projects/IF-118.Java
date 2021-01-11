package com.softserve.itacademy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
public class UserRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
