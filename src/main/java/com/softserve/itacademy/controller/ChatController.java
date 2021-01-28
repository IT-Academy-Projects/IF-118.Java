package com.softserve.itacademy.controller;


import static com.softserve.itacademy.config.Constance.API_V1;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.ChatMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(API_V1 + "chat")
public class ChatController {

    public static final int CHAT_PAGE_SIZE = 50;

    private final ChatMessageService chatMessageService;

    public ChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/{chatId}")
    @PreAuthorize("@accessManager.isAllowedToChat(#user, #chatId)")
    public ChatMessageResponse processMessage(
            @Payload @Valid ChatMessageRequest chatMessageRequest,
            @DestinationVariable Integer chatId,
            @AuthenticationPrincipal User user) {

        return chatMessageService.processMessage(chatMessageRequest, user, chatId);
    }

    @GetMapping("/{chatId}/{pageNo}")
    @PreAuthorize("@accessManager.isAllowedToChat(authentication.principal, #chatId)")
    public ResponseEntity<List<ChatMessageResponse>> getPaginatedMessages(
            @PathVariable int chatId,
            @PathVariable int pageNo) {

        return new ResponseEntity<>(chatMessageService.findPaginatedByChatRoomId(pageNo, CHAT_PAGE_SIZE, chatId), HttpStatus.OK);
    }
}
