package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.EventRepository;
import com.softserve.itacademy.response.EventResponse;
import com.softserve.itacademy.service.EventService;
import com.softserve.itacademy.service.converters.EventConverter;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventConverter eventConverter;

    public EventServiceImpl(EventRepository eventRepository, EventConverter eventConverter) {
        this.eventRepository = eventRepository;
        this.eventConverter = eventConverter;
    }

    @Override
    public EventResponse findById(Integer id) {
        return eventConverter.of(eventRepository.findById(id).orElseThrow(() -> new NotFoundException("Event not found")));
    }
}
