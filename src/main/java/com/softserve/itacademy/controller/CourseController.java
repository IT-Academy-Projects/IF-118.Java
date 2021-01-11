package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.converters.CourseConverter;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest courseRequest,
                                                 @AuthenticationPrincipal User currentUser) {
        courseRequest.setOwnerId(currentUser.getId());
        return new ResponseEntity<>(courseService.create(courseRequest), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> findByOwner(@AuthenticationPrincipal User currentUser) {
        return new ResponseEntity<>(courseService.findByOwner(currentUser.getId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disabledRequest) {
        courseService.updateDisabled(id, disabledRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    //TODO create readByID and update courses

}
