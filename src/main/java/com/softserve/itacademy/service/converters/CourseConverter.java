package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.request.CourseRequest;
import com.softserve.itacademy.response.CourseResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
        map.setMaterialIds((course.getMaterials().stream()
                .map(Material::getId)
                .collect(Collectors.toSet())));
        map.setHasAvatar(course.getAvatar() != null);
        return map;
    }

    public Course of(CourseRequest courseRequest, Set<Material> materials) {
        Course map = mapper.map(courseRequest, Course.class);
        map.setId(null);
        map.setDisabled(false);
        map.setGroups(Collections.emptySet());
        map.setMaterials(materials);
        return map;
    }
}
