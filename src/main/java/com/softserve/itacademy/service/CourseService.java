package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CourseService {
    CourseResponse create(CourseRequest courseRequest, MultipartFile file);
    CourseResponse readById(Integer id);
    CourseResponse update(CourseRequest courseRequest);
    List<CourseResponse> findAll();
    List<CourseResponse> findByOwner(Integer id);
    Course getById(Integer id);
    void updateDisabled(Integer id, boolean disabled);
    void updateDescription(Integer id, String description);
    byte[] getAvatarById(Integer id);
}
