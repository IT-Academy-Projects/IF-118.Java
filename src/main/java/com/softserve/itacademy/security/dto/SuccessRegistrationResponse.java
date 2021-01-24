package com.softserve.itacademy.security.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class SuccessRegistrationResponse {

    private String name;
    private String email;
    private String role;

}
