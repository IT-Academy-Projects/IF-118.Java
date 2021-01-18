package com.softserve.itacademy.projection;

import com.softserve.itacademy.entity.ChatMessage;


public interface ChatMessageTinyProjection extends IdProjection {

    String getContent();
    ChatMessage.MessageStatus getStatus();
    UserTinyProjection getUser();
}
