package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.response.EventResponse;

import java.util.List;

public interface EventService {

    void sendNotificationFromEvent(Event event);
    List<EventResponse> findPaginatedByUserId(int userId, int pageNo);

}
