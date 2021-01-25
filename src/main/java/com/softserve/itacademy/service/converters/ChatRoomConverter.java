package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.repository.ChatRoomRepository;
import com.softserve.itacademy.response.ChatRoomResponse;
import com.softserve.itacademy.response.GroupResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@AllArgsConstructor
@Component
public class ChatRoomConverter {

    private final ModelMapper mapper;

    public ChatRoomResponse of(ChatRoom chatRoom) {
        return mapper.map(chatRoom, ChatRoomResponse.class);
    }
}
