package com.softserve.itacademy.controller;

import com.softserve.itacademy.response.EventResponse;
import com.softserve.itacademy.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.softserve.itacademy.config.Constance.API_V1;

@RestController
@RequestMapping(API_V1 + "events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<EventResponse>> findAllByUserId(@PathVariable Integer userId, @RequestParam Integer pageNo) {
        return new ResponseEntity<>(eventService.findPaginatedByUserId(userId, pageNo), HttpStatus.OK);
    }

}
