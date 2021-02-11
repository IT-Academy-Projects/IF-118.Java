package com.softserve.itacademy.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChatMessageRequest {

    @Length(min = 1, max = 4096)
    private String content;

    @JsonCreator
    public ChatMessageRequest(@Length(min = 1, max = 4096) String content) {
        this.content = content;
    }
}
