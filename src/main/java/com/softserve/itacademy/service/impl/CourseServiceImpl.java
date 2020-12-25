package com.softserve.itacademy.service.impl;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.dto.CourseDto;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        Course course = CourseDto.convertToEntity(courseDto);
        Course createdCourse = courseRepository.save(course);
        return CourseDto.convertToDto(createdCourse);
    }

    @Override
    public CourseDto readById(long id) {
        return null;
    }

    @Override
    public CourseDto update(CourseDto courseDto) {
        return null;
    }

    @Override
    public void delete(long id) {

    }

    @Override
    public List<CourseDto> getAll() {
        return courseRepository.findAll().stream()
                .map(CourseDto::convertToDto)
                .collect(Collectors.toList());
    }
}
