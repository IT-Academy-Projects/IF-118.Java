package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.dto.GroupDto;
import com.softserve.itacademy.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MySqlGroupService implements GroupService {

    private final GroupRepository groupRepository;

    public MySqlGroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<GroupDto> findAll() {
        return groupRepository.findAll().stream().map(GroupDto::create).collect(Collectors.toList());
    }

    @Override
    public Boolean delete(Integer id) {
        if (groupRepository.existsById(id)) {
            groupRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void updateDisabled(Integer id, Boolean disabled) {
        Group group = groupRepository.findById(id).orElseThrow(RuntimeException::new);
        group.setDisabled(disabled);
        groupRepository.save(group);
    }

}
