package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class CourseConverter {

    private final ModelMapper mapper;

    public CourseResponse of(Course course) {
        CourseResponse map = mapper.map(course, CourseResponse.class);
        map.setGroupIds((course.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toSet())));
        return map;
    }

    public Course of(CourseRequest courseDto, Set<Group> groups) {
        Course map = mapper.map(courseDto, Course.class);
        map.setDisabled(false);
        map.setGroups(groups);
        return map;
    }
}
