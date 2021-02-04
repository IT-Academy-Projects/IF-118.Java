package com.softserve.itacademy.response;

import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.projection.UserTinyProjection;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponse {

    private Integer id;
    private Event.EventType type;
    private UserTinyProjection creator;
    private UserTinyProjection recipient;
    private LocalDateTime date;
    private Integer subjectId;

}
