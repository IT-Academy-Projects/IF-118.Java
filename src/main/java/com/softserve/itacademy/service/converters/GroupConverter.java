package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class GroupConverter {

    private final ModelMapper mapper;
    private final CourseConverter courseConverter;
    private final UserConverter userConverter;

    public GroupResponse of(Group group) {
        GroupResponse map = mapper.map(group, GroupResponse.class);
        map.setCourses(group.getCourses().stream()
                .map(courseConverter::of)
                .collect(Collectors.toSet()));
        map.setUsers(group.getUsers().stream()
                .filter(user -> !user.getId().equals(group.getOwnerId()) && !user.getDisabled())
                .map(userConverter::of)
                .collect(Collectors.toSet()));
        map.setHasAvatar(group.getAvatar() != null);
        return map;
    }

    public Group of(GroupRequest groupRequest, Set<Course> courses) {
        Group map = mapper.map(groupRequest, Group.class);
        map.setId(null);
        map.setDisabled(false);
        map.setCourses(courses);
        return map;
    }
}
