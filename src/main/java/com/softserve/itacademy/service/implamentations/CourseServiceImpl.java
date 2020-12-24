package com.softserve.itacademy.service.implamentations;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.dto.CourseDto;
import com.softserve.itacademy.exception.NotFoundException;
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
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream().map(CourseDto::create).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        courseRepository.delete(getById(id));
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        Course group = getById(id);
        group.setDisabled(disabled);
        courseRepository.save(group);
    }

    private Course getById(Integer id) {
        return courseRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
