package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.Course;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CourseDto {

    private Integer id;
    private String name;
    private String ownerId;
    private Boolean disabled;

    public static CourseDto create(Course course) {
        return CourseDto
                .builder()
                .id(course.getId())
                .name(course.getName())
                .disabled(course.getDisabled())
                .build();
    }
}
