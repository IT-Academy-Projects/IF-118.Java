package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.CourseDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> findAll();
    void delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
}
