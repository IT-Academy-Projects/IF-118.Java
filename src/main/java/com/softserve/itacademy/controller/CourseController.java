package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
