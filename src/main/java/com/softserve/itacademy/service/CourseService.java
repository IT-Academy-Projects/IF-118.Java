package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.dto.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto create(CourseDto courseDto);
    CourseDto readById(long id);
    CourseDto update(CourseDto courseDto);
    void delete(long id);
    List<CourseDto> getAll();
}
