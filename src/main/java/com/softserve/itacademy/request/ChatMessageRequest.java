package com.softserve.itacademy.request;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChatMessageRequest {

    @Length(min = 1, max = 4096)
    private String content;
}
