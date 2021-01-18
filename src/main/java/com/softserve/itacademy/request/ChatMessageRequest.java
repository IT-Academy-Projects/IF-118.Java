package com.softserve.itacademy.request;

import lombok.Data;

import javax.validation.constraints.Size;

@Data
public class ChatMessageRequest {

    @Size(max=255)
    private String content;

    private Integer groupId;
}
