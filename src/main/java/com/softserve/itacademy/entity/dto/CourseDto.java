package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import liquibase.pro.packaged.C;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
public class CourseDto {
    private Integer id;
    private String name;
    private Integer ownerId;
    private Set<Integer> groupIds;
    private Set<Integer> userIds;

    public static CourseDto convertToDto(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.setId(course.getId());
        courseDto.setName(course.getName());
        courseDto.setOwnerId(course.getOwnerId());
        courseDto.setGroupIds(course.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toSet()));
        courseDto.setUserIds(course.getUsers().stream()
                .map(User::getId)
                .collect(Collectors.toSet()));
        return courseDto;
    }
    public static Course convertToEntity(CourseDto courseDto) {
        Course course = new Course();
        course.setName(courseDto.getName());
        course.setOwnerId(courseDto.getOwnerId());
        course.setGroups(new HashSet<>());
        course.setUsers(new HashSet<>());
        return course;
    }
}
