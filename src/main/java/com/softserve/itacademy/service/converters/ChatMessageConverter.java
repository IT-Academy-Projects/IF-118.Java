package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.response.CourseResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class ChatMessageConverter {

    private final ModelMapper mapper;

    public ChatMessageResponse of(ChatMessage chatMessage) {

        return mapper.map(chatMessage, ChatMessageResponse.class);
    }
}
