package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/create")
    public ResponseEntity<CourseResponse> create(@RequestBody CourseRequest courseDto) {
        return new ResponseEntity<>(courseService.create(courseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> findAll() {
        return new ResponseEntity<>(courseService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return new ResponseEntity<>(OK);
    }

    @PatchMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestBody DisableRequest disabledRequest) {
        courseService.updateDisabled(id, disabledRequest.isDisabled());
        return new ResponseEntity<>(OK);
    }

    //TODO create readByID and update courses

}
