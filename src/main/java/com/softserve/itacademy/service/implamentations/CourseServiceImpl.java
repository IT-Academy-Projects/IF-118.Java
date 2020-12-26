package com.softserve.itacademy.service.implamentations;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.dto.CourseDto;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.service.CourseService;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.UserService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final GroupService groupService;
    private final UserService userService;

    public CourseServiceImpl(CourseRepository courseRepository, GroupService groupService, UserService userService) {
        this.courseRepository = courseRepository;
        this.groupService = groupService;
        this.userService = userService;
    }

    @Override
    public CourseDto create(CourseDto courseDto) {
        userService.findById(courseDto.getOwnerId());   //check if ownerId is valid
        Set<Integer> groupIds = Optional.ofNullable(courseDto.getGroupIds())
                .orElse(new HashSet<>());

        Set<Group> groups = groupIds.stream()
                .map(groupService::findById)
                .collect(Collectors.toSet());
        Course course = CourseDto.convertToEntity(courseDto);
        course.setGroups(groups);
        Course createdCourse = courseRepository.save(course);
        return CourseDto.convertToDto(createdCourse);
    }

    @Override
    public List<CourseDto> findAll() {
        return courseRepository.findAll().stream()
                .map(CourseDto::convertToDto)
                .collect(Collectors.toList());
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

    @Override
    public CourseDto readById(Integer id) {
        Course course = courseRepository.findById(id).orElseThrow(NotFoundException::new);
        return CourseDto.convertToDto(course);
    }

    @Override
    public CourseDto update(CourseDto courseDto) {
        return null;
    }

    private Course getById(Integer id) {
        return courseRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
