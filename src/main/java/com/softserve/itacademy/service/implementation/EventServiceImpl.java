package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.repository.EventRepository;
import com.softserve.itacademy.response.EventResponse;
import com.softserve.itacademy.service.EventService;
import com.softserve.itacademy.service.converters.EventConverter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;
    private final SimpMessagingTemplate messagingTemplate;

    public EventServiceImpl(EventRepository eventRepository,
                            EventConverter eventConverter,
                            SimpMessagingTemplate messagingTemplate) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendNotificationFromEvent(Event event) {
        EventResponse response = eventConverter.of(event);
        messagingTemplate.convertAndSend("/api/v1/ws/event/notification/" + event.getRecipient().getId(), response);
    }

    @Override
    public List<EventResponse> findPaginatedByUserId(int userId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10, Sort.by("date").descending());
        Page<Event> pagedResult = eventRepository.findPaginatedByRecipientId(userId, pageable);
        return pagedResult.stream().map(eventConverter::of).collect(Collectors.toList());
    }
}
