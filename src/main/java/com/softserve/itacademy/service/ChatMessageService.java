package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageService {

    @Transactional
    ChatMessageResponse processMessage(ChatMessageRequest chatMessageRequest, User user, Integer chatId);

    ChatMessage save(ChatMessage chatMessage);

    List<ChatMessageResponse> findPaginatedByChatRoomId(int pageNo, int pageSize, int chatId);
}
