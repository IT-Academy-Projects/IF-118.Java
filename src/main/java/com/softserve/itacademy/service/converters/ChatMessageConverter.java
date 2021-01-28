package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.projection.UserTinyProjection;
import com.softserve.itacademy.response.ChatMessageResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class ChatMessageConverter {


    private final ModelMapper mapper;
    private final ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();

    public ChatMessageResponse of(ChatMessage chatMessage) {
        ChatMessageResponse map = mapper.map(chatMessage, ChatMessageResponse.class);

        UserTinyProjection user = projectionFactory.createProjection(UserTinyProjection.class, chatMessage.getUser());
        map.setUser(user);

        return map;
    }

    public Collection<ChatMessageResponse> of(Collection<ChatMessage> messages) {
        return messages.stream()
                .map(this::of)
                .collect(Collectors.toList());
    }
}
