package com.softserve.itacademy.exception.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasicExceptionResponse {

    @Builder.Default()
    private LocalDateTime timestamp = LocalDateTime.now();

    private String error;
    private String message;
    private Integer status;

}
