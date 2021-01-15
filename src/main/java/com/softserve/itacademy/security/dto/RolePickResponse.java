package com.softserve.itacademy.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RolePickResponse {

    String email;
    String pickedRole;

    @Builder.Default
    Boolean success = true;
}
