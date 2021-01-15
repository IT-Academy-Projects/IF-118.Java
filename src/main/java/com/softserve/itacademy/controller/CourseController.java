package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DescriptionRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.security.perms.CourseCreatePermission;
import com.softserve.itacademy.security.perms.CourseDeletePermission;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.CourseUpdatePermission;
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

    @CourseCreatePermission
    @PostMapping
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest courseRequest,
                                                 @AuthenticationPrincipal User currentUser) {
        courseRequest.setOwnerId(currentUser.getId());
        return new ResponseEntity<>(courseService.create(courseRequest), HttpStatus.CREATED);
    }

    @CourseReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(courseService.readById(id), HttpStatus.OK);
    }

    @CourseReadPermission
    @GetMapping
    public ResponseEntity<List<CourseResponse>> findByOwner(@AuthenticationPrincipal User currentUser) {
        return new ResponseEntity<>(courseService.findByOwner(currentUser.getId()), HttpStatus.OK);
    }

    @CourseDeletePermission
    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disabledRequest) {
        courseService.updateDisabled(id, disabledRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    @CourseUpdatePermission
    @PatchMapping("/{id}/description")
    public ResponseEntity<Void> updateDescription(@PathVariable Integer id, @RequestBody DescriptionRequest descriptionRequest) {
        courseService.updateDescription(id, descriptionRequest.getDescription());
        return new ResponseEntity<>(OK);
    }

    //TODO create readByID and update courses

}
