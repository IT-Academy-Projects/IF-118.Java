package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.dto.CourseDto;
import com.softserve.itacademy.entity.dto.UserDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> findAll();
    Boolean delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
}
