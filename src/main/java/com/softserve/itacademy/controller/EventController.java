package com.softserve.itacademy.controller;

import com.softserve.itacademy.response.EventResponse;
import com.softserve.itacademy.security.perms.roles.UserRolePermission;
import com.softserve.itacademy.service.EventService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.softserve.itacademy.config.Constance.API_V1;

@RestController
@RequestMapping(API_V1 + "events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @UserRolePermission
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(eventService.findById(id), HttpStatus.OK);
    }

}
