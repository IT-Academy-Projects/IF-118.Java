package com.softserve.itacademy.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PasswordByTokenRequest {

    String newPassword;
    String token;
}
