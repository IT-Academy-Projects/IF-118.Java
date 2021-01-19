package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Course;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.GroupConverter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;
    private final UserService userService;
    private final CourseRepository courseRepository;

    public GroupServiceImpl(GroupRepository groupRepository, GroupConverter groupConverter, UserService userService, CourseRepository courseRepository) {
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
        this.userService = userService;
        this.courseRepository = courseRepository;
    }

    @Override
    public GroupResponse create(GroupRequest groupRequest) {
        User owner = userService.getById(groupRequest.getOwnerId());

        if (owner.getDisabled()) {
            throw new DisabledObjectException("Group is disabled");
        }
        Set<Integer> courseIds = groupRequest.getCourseIds();
        Group newGroup;
        if (courseIds == null) {
            newGroup = groupConverter.of(groupRequest, Collections.emptySet());
        } else {
            Set<Course> courses = courseIds.stream()
                    .map(id -> courseRepository.findById(id).get())
                    .collect(Collectors.toSet());
            newGroup = groupConverter.of(groupRequest, courses);
            courses.forEach(course -> course.getGroups().add(newGroup));
        }
        return groupConverter.of(groupRepository.save(newGroup));
    }

    @Override
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream()
                .map(groupConverter::of).collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findByOwner(Integer id) {
        return groupRepository.findByOwnerId(id).stream()
                .map(groupConverter::of)
                .collect(Collectors.toList());
    }

    @Override
    public void updateGroup(Integer groupId, GroupRequest groupRequest) {
        Group group = groupRepository.findByIdAndOwnerId(groupId, groupRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException("Group with such id was not found"));
        group.setName(groupRequest.getName());
    }

    @Override
    public void updateDisabled(Integer id, boolean disabled) {
        if (groupRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException("Group with such id is not found");
        }
    }

    @Override
    public GroupResponse findById(Integer id) {
        return groupConverter.of(getById(id));
    }

    private Group getById(Integer id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group with such id was not found"));
    }
}
