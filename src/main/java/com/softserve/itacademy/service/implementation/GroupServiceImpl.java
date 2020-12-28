package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.dto.GroupDto;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.service.GroupService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;

    public GroupServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> findAll() {
        return groupRepository.findAll().stream().map(GroupDto::create).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        groupRepository.delete(getById(id));
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        Group group = getById(id);
        group.setDisabled(disabled);
        groupRepository.save(group);
    }

    @Override
    public Group findById(Integer id) {
        return getById(id);
    }

    private Group getById(Integer id) {
        return groupRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}
