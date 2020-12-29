package com.softserve.itacademy.service;

import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;

import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest courseDto);
    CourseResponse readById(Integer id);
    CourseResponse update(CourseRequest courseDto);
    List<CourseResponse> findAll();
    void delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
}
