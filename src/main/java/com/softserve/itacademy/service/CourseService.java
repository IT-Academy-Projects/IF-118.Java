package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.CourseDto;
import com.softserve.itacademy.entity.dto.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto create(CourseDto courseDto);
    CourseDto readById(long id);
    CourseDto update(CourseDto courseDto);
    List<CourseDto> findAll();
    void delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
}
