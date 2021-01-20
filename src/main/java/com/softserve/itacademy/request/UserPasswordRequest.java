package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserPasswordRequest {
    private String oldPassword;
    private String newPassword;
}
