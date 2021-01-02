package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.request.DisableRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;


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

    @GetMapping("/teacher/{id}")
    public ResponseEntity<List<CourseResponse>> findByOwner(@PathVariable("id") Integer ownerId) {
        return new ResponseEntity<>(courseService.findByOwnerId(ownerId), OK);

    }
    //todo url naming
    @GetMapping("/student/{id}")
    public ResponseEntity<List<CourseResponse>> find(@PathVariable("id") Integer id) {
        return new ResponseEntity<>(courseService.findByStudentId(id), OK);
    }


    //TODO create readByID and update courses

}
