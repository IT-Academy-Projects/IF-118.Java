package com.softserve.itacademy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
public class CommentRequest {
    @NotBlank
    private String message;
    private Integer ownerId;
    private Integer materialId;
    private Boolean isPrivate;
}
