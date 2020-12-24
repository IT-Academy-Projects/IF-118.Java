package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.Course;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDto {
    private Integer id;
    private String name;
    private String ownerId;

    private CourseDto() {};

    public static CourseDto create(Course course) {
        CourseDto courseDto = new CourseDto();
        courseDto.id = course.getId();
        courseDto.name = course.getName();
        return courseDto;
    }
}
