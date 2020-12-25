package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class CourseDto {

    private Integer id;
    private String name;
    private Integer ownerId;
    private boolean disabled;
    private Set<Integer> groupIds;

    public static CourseDto convertToDto(Course course) {
        return CourseDto.builder()
                .id(course.getId())
                .name(course.getName())
                .disabled(course.isDisabled())
                .ownerId(course.getOwnerId())
                .groupIds(course.getGroups().stream()
                        .map(Group::getId)
                        .collect(Collectors.toSet()))
                .build();
    }

    public static Course convertToEntity(CourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setOwnerId(courseDto.getOwnerId());
        return course;
    }
}
