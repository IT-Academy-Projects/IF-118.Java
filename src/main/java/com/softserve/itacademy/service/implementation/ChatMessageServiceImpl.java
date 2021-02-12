package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.repository.ChatMessageRepository;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.ChatMessageService;
import com.softserve.itacademy.service.ChatRoomService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.ChatMessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;
    private final UserService userService;
    private final ChatMessageConverter chatMessageConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, ChatRoomService chatRoomService,
            UserService userService, ChatMessageConverter chatMessageConverter,
            SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomService = chatRoomService;
        this.userService = userService;
        this.chatMessageConverter = chatMessageConverter;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    @Override
    public ChatMessageResponse processMessage(ChatMessageRequest chatMessageRequest, Integer userId, Integer chatId) {
        log.info("Received new message with for chat id {} from user id {}", chatId, userId);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageRequest.getContent())
                .chatRoom(chatRoomService.getById(chatId))
                .user(userService.getById(userId))
                .status(ChatMessage.MessageStatus.RECEIVED)
                .build();

        chatMessage = save(chatMessage);
        return sendMessage(chatMessage);
    }

    @Override
    public List<ChatMessageResponse> findPaginatedByChatRoomId(int pageNo, int pageSize, int chatId) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());
        Page<ChatMessage> pagedResult = chatMessageRepository.findPaginatedByChatRoomId(paging, chatId);
        List<ChatMessageResponse> list = (List<ChatMessageResponse>) chatMessageConverter.of(pagedResult.toList());

        Collections.reverse(list);
        return list;
    }

    private ChatMessage save(ChatMessage chatMessage) {
        chatMessage = chatMessageRepository.save(chatMessage);
        log.info("Saved message with id {}", chatMessage.getId());
        return chatMessage;
    }

    private ChatMessageResponse sendMessage(ChatMessage chatMessage) {
        ChatMessageResponse response = chatMessageConverter.of(chatMessage);

        messagingTemplate.convertAndSend("/api/v1/ws/event/chat/" + chatMessage.getChatRoom().getId(), response);
        chatMessage.setStatus(ChatMessage.MessageStatus.DELIVERED);
        chatMessageRepository.save(chatMessage);

        log.info("Broadcast message with id {}", chatMessage.getId());

        return response;
    }

}
