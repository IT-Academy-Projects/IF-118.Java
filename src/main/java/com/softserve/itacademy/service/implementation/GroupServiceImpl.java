package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.response.GroupResponse;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.converters.GroupConverter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository groupRepository;
    private final GroupConverter groupConverter;

    @Override
    public List<GroupResponse> findAll() {
        return groupRepository.findAll().stream()
                .map(groupConverter::convertToDto).collect(Collectors.toList());
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
