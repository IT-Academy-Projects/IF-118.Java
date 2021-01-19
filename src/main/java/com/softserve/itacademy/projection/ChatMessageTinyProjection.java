package com.softserve.itacademy.projection;

import com.softserve.itacademy.entity.ChatMessage;

import java.time.LocalDateTime;


public interface ChatMessageTinyProjection extends IdProjection {

    String getContent();
    LocalDateTime getCreatedAt();
    ChatMessage.MessageStatus getStatus();
    UserTinyProjection getUser();
}
