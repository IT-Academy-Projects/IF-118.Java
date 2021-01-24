package com.softserve.itacademy.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Size;

@Data
public class ChatMessageRequest {

    @Length(min = 1, max = 255)
    private String content;
}
