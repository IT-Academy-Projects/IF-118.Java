package com.softserve.itacademy.security.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class RegistrationRequest {

//    TODO bk this is not a good practice to keep such an error messages here. I'll explain why
    @NotBlank(message = "Email cannot be empty\"")
    @Email(message = "Email is invalid")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$",
            message = "Password should be minimum eight characters and contain atl east one letter and one number "
    )
    private String password;

    @NotBlank(message = "Name cannot be empty")
    @Length(min = 3, max = 30, message = "Username should be between 3 and 30 characters long")
    private String name;

    @NotBlank(message = "Pick a role")
    private String pickedRole;


}
