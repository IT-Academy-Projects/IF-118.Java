package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.ChatMessageTinyProjection;
import com.softserve.itacademy.repository.ChatMessageRepository;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.ChatMessageService;
import com.softserve.itacademy.service.GroupChatService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.ChatMessageConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final GroupChatService groupChatService;
    private final UserService userService;
    private final ChatMessageConverter chatMessageConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository, GroupChatService groupChatService, UserService userService, ChatMessageConverter chatMessageConverter, SimpMessagingTemplate messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.groupChatService = groupChatService;
        this.userService = userService;
        this.chatMessageConverter = chatMessageConverter;
        this.messagingTemplate = messagingTemplate;
    }

    @Transactional
    @Override
    public ChatMessageResponse processMessage(ChatMessageRequest chatMessageRequest, User user) {

        ChatMessage chatMessage = ChatMessage.builder()
                .content(chatMessageRequest.getContent())
                .groupChat(groupChatService.getById(chatMessageRequest.getGroupId()))
                .user(userService.getById(user.getId()))
                .status(ChatMessage.MessageStatus.RECEIVED)
                .build();

        ChatMessageResponse response = chatMessageConverter.of(chatMessage);
        response.setUsername(user.getName());

        messagingTemplate.convertAndSend("/group/" + chatMessageRequest.getGroupId() + "/chat/sub", response);
        chatMessage.setStatus(ChatMessage.MessageStatus.DELIVERED);
        save(chatMessage);

        return response;
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @Override
    public List<ChatMessageTinyProjection> findPaginatedByGroupChatId(int pageNo, int pageSize, int chatId) {

        Pageable paging = PageRequest.of(pageNo, pageSize);
        Page<ChatMessageTinyProjection> pagedResult = chatMessageRepository.findPaginatedByGroupChatId(paging, chatId);

        return pagedResult.toList();
    }

}
