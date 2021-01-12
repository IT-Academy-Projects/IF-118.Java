package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.GroupConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;
    private final UserService userService;

    public GroupServiceImpl(GroupRepository groupRepository, GroupConverter groupConverter, UserService userService) {
        this.groupRepository = groupRepository;
        this.groupConverter = groupConverter;
        this.userService = userService;
    }

    @Override
    public GroupResponse create(GroupRequest groupRequest){
        User owner = userService.getById(groupRequest.getOwnerId());

        if (owner.getDisabled()) {
            throw new DisabledObjectException("Object disabled");
        }

        Group newGroup = groupConverter.of(groupRequest);

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
    public void updateDisabled(Integer id, boolean disabled) {
        if (groupRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException();
        }
    }

    @Override
    public GroupResponse findById(Integer id) {
        return groupConverter.of(getById(id));
    }

    //TODO make private
    private Group getById(Integer id) {
        return groupRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
