package com.softserve.itacademy.projection;

import com.softserve.itacademy.entity.ChatMessage;

import java.time.LocalDateTime;


public interface ChatMessageProjection extends IdProjection {

    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    ChatMessage.MessageStatus getStatus();
    UserTinyProjection getUser();
}
