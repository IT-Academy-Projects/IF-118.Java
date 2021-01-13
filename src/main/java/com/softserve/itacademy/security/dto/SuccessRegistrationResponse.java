package com.softserve.itacademy.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessRegistrationResponse {

    private String name;
    private String email;

}
