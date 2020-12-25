package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.dto.CourseDto;
import com.softserve.itacademy.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<CourseDto> findAll() {
        return courseService.getAll();
    }

    @PostMapping("/create")
    public CourseDto create(@RequestBody CourseDto courseDto) {
        return courseService.create(courseDto);
    }
}
