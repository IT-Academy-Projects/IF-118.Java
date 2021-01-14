package com.softserve.itacademy.controller;


import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.service.ChatMessageService;
import com.softserve.itacademy.service.GroupChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupChatController {

    private final GroupChatService groupChatService;

    private final ChatMessageService chatMessageService;

    public GroupChatController(GroupChatService groupChatService, ChatMessageService chatMessageService) {
        this.groupChatService = groupChatService;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/group/{groupId}/chat")
    public void processMessage(ChatMessageRequest chatMessageRequest) {
        // var chatRoom = chatMessage.getGroupChat();

        var chatMessage = ChatMessage.builder().content(chatMessageRequest.getContent()).build();

        ChatMessage saved = chatMessageService.save(chatMessage);

    }
}
