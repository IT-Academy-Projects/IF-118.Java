package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.EventRepository;
import com.softserve.itacademy.response.EventResponse;
import com.softserve.itacademy.service.converters.EventConverter;
import com.softserve.itacademy.service.implementation.EventServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventConverter eventConverter;

    @Mock
    SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void testFindPaginatedByUserId() {
        when(eventRepository.findPaginatedByRecipientId(anyInt(), any(Pageable.class))).thenReturn(mockPageEvent());
        when(eventConverter.of(any(Event.class))).thenReturn(mock(EventResponse.class));
        List<EventResponse> responses = eventService.findPaginatedByUserId(1, 0);
        verify(eventRepository, times(1)).findPaginatedByRecipientId(anyInt(), any(Pageable.class));
        verify(eventConverter, times(responses.size())).of(any(Event.class));
    }

    @Test
    void testSendNotificationFromEvent() {
        Event event = mockEvent();
        doNothing().when(messagingTemplate).convertAndSend(anyString(), any(Object.class));
        when(eventConverter.of(any(Event.class))).thenReturn(mock(EventResponse.class));
        eventService.sendNotificationFromEvent(event);
        verify(eventConverter, times(1)).of(event);
        verify(messagingTemplate, times(event.getRecipients().size())).convertAndSend(anyString(), any(Object.class));
    }

    private Page<Event> mockPageEvent() {
        return new PageImpl<>(List.of(new Event(), new Event()));
    }

    private Event mockEvent() {
        return Event.builder()
                .creator(new User())
                .type(Event.EventType.INVITE)
                .entityId(1)
                .recipients(List.of(new User(), new User()))
                .build();
    }

}