package com.softserve.itacademy.service;

import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatMessageService {

    @Transactional
    ChatMessageResponse processMessage(ChatMessageRequest chatMessageRequest, Integer userId, Integer chatId);

    List<ChatMessageResponse> findPaginatedByChatRoomId(int pageNo, int pageSize, int chatId);
}
