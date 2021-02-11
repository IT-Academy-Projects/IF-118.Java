package com.softserve.itacademy.response;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.UserTinyProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventResponse {

    private Integer id;
    private Event.EventType type;
    private UserTinyProjection creator;
    private List<User> recipients;
    private LocalDateTime date;
    private Integer entityId;

}
