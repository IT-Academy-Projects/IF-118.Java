package com.softserve.itacademy.service;

import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import com.softserve.itacademy.entity.Course;
import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest courseDto);
    CourseResponse readById(Integer id);
    CourseResponse update(CourseRequest courseDto);
    List<CourseResponse> findAll();
    Course getById(Integer id);
    void delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
}
