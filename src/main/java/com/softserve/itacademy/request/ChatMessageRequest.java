package com.softserve.itacademy.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ChatMessageRequest {

    @Size(min=1, max=255)
    private String content;

    private Integer groupId;
}
