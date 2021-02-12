package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.UserTinyProjection;
import com.softserve.itacademy.response.EventResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventConverter {

    private final ModelMapper mapper;
    private final ProjectionFactory projectionFactory;

    public EventConverter(ModelMapper mapper, ProjectionFactory projectionFactory) {
        this.mapper = mapper;
        this.projectionFactory = projectionFactory;
    }

    public EventResponse of(Event event) {
        EventResponse map = mapper.map(event, EventResponse.class);

        UserTinyProjection creator = projectionFactory.createProjection(UserTinyProjection.class, event.getCreator());
        List<Integer> recipients = event.getRecipients().stream()
                .map(User::getId).collect(Collectors.toList());

        map.setCreator(creator);
        map.setRecipientIds(recipients);

        return map;
    }

}
