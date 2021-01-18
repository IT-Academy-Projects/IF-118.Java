package com.softserve.itacademy.controller;


import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.ChatMessageTinyProjection;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.ChatMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping
public class GroupChatController {

    private final int PAGE_SIZE = 20;

    private final ChatMessageService chatMessageService;

    public GroupChatController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/group/{groupId}/chat")
    @PreAuthorize("@decider.checkIfGroupMember(#user, #chatMessageRequest.getGroupId())")
    public ChatMessageResponse processMessage(
            @Payload @Valid ChatMessageRequest chatMessageRequest,
           // @DestinationVariable Integer groupId, //TODO
            @AuthenticationPrincipal User user) {

        return chatMessageService.processMessage(chatMessageRequest, user);
    }

    @GetMapping("/chat/{pageNo}")
    @PreAuthorize("@decider.checkIfGroupMember(authentication.principal, #groupId)")
    public ResponseEntity<List<ChatMessageTinyProjection>> getPaginatedCountries(
            @PathVariable int pageNo,
            @RequestParam(name="id") int groupId) { //TODO

        return new ResponseEntity<>(chatMessageService.findPaginatedByGroupChatId(pageNo, PAGE_SIZE, groupId), OK);
    }
}
