package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.dto.CourseDto;
import com.softserve.itacademy.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MySqlCourseService implements CourseService {

    private final CourseRepository courseRepository;

    public MySqlCourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream().map(CourseDto::create).collect(Collectors.toList());
    }

    @Override
    public Boolean delete(Integer id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
