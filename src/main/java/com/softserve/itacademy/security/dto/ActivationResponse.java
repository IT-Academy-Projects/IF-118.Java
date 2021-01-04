package com.softserve.itacademy.security.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActivationResponse {

    private String message;

    private boolean isActivated;

}
