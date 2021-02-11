package com.softserve.itacademy.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
public class ChatMessageRequest {

    @Length(min = 1, max = 4096)
    private String content;
}
