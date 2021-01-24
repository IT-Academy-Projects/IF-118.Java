package com.softserve.itacademy.security.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ActivationResponse {

    private String message;
    private boolean isActivated;

}
