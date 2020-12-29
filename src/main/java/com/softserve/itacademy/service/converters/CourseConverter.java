package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CourseConverter {

    public CourseResponse convertToDto(Course course) {
        return CourseResponse.builder()
                .name(course.getName())
                .ownerId(course.getOwnerId())
                .disabled(course.getDisabled())
                .groupIds(course.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    public Course convertToCourse(CourseRequest courseDto, Set<Group> groups) {
        return Course.builder()
                .name(courseDto.getName())
                .ownerId(courseDto.getOwnerId())
                .disabled(false)
                .groups(groups)
                .build();
    }
}
