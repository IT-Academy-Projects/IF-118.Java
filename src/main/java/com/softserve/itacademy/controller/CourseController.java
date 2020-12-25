package com.softserve.itacademy.controller;

import com.softserve.itacademy.dto.CourseDto;
import com.softserve.itacademy.entity.dto.CourseDto;
import com.softserve.itacademy.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/courses")
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

    @GetMapping
    public ResponseEntity<List<CourseDto>> findAll() {
        return new ResponseEntity<>(courseService.findAll(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        courseService.delete(id);
        return new ResponseEntity<>(OK);
    }

    @DeleteMapping("/{id}/disabled")
    public ResponseEntity<Void> updateDisabled(@PathVariable Integer id, @RequestParam Boolean disabled) {
        courseService.updateDisabled(id, disabled);
        return new ResponseEntity<>(OK);
    }

}
