package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.response.ChatRoomResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@AllArgsConstructor
@Component
public class ChatRoomConverter {

    private final ModelMapper mapper;

    public ChatRoomResponse of(ChatRoom chatRoom) {
        return mapper.map(chatRoom, ChatRoomResponse.class);
    }
}
