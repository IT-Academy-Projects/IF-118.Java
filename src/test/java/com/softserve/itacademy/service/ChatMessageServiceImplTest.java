package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.ChatMessage;
import com.softserve.itacademy.entity.ChatRoom;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.UserTinyProjection;
import com.softserve.itacademy.repository.ChatMessageRepository;
import com.softserve.itacademy.request.ChatMessageRequest;
import com.softserve.itacademy.response.ChatMessageResponse;
import com.softserve.itacademy.service.converters.ChatMessageConverter;
import com.softserve.itacademy.service.implementation.ChatMessageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ChatMessageServiceImplTest {

    private final ProjectionFactory factory = new SpelAwareProxyProjectionFactory();

    @InjectMocks
    private ChatMessageServiceImpl chatMessageService;
    @Mock
    private ChatMessageRepository chatMessageRepository;
    @Mock
    private ChatRoomService chatRoomService;
    @Mock
    private UserService userService;
    @Mock
    private ChatMessageConverter chatMessageConverter;
    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @BeforeEach
    public void setup() {

        when(chatMessageRepository.save(Mockito.any(ChatMessage.class))).thenAnswer(
                invocation -> {
                    ChatMessage message = invocation.getArgument(0);
                    message.setUpdatedAt(LocalDateTime.now());
                    message.setCreatedAt(LocalDateTime.now());
                    message.setId(1);
                    return message;
                }
        );

        when(chatMessageConverter.of(Mockito.any(ChatMessage.class))).thenAnswer(
                invocation -> {
                    ChatMessage message = invocation.getArgument(0);
                    return ChatMessageResponse.builder()
                            .content(message.getContent())
                            .id(message.getId())
                            .createdAt(message.getCreatedAt())
                            .updatedAt(message.getUpdatedAt())
                            .user(factory.createProjection(UserTinyProjection.class, message.getCreatedAt()))
                            .build();
                }
        );
    }

    @Test
    void testProcessMessageSuccess() {
        when(userService.getById(getUser().getId())).thenReturn(getUser());
        when(chatRoomService.getById(getUser().getId())).thenReturn(getChatRoom());

        ChatMessageResponse exceptedResponse = ChatMessageResponse.builder()
                .content(getRequest().getContent())
                .id(1)
                .user(factory.createProjection(UserTinyProjection.class, getUser()))
                .build();

        ChatMessageResponse actualResponse = chatMessageService.processMessage(getRequest(), getUser().getId(), getChatRoom().getId());

        assertEquals(exceptedResponse, actualResponse);
        verify(chatMessageRepository, times(2)).save(isA(ChatMessage.class));
        verify(messagingTemplate, times(1)).convertAndSend(anyString(), eq(exceptedResponse));
    }

    private User getUser() {
        User user = User.builder()
                .email("test@example.com")
                .name("tester")
                .activated(false)
                .pickedRole(false)
                .activationCode("testcode")
                .build();
        user.setId(1);
        return user;
    }

    private ChatRoom getChatRoom() {
        ChatRoom chatRoom = ChatRoom.builder()
                .messages(Collections.emptyList())
                .type(ChatRoom.ChatType.GROUP)
                .build();
        chatRoom.setId(1);
        return chatRoom;
    }

    private ChatMessageRequest getRequest() {
        return new ChatMessageRequest("Test message");
    }
}
