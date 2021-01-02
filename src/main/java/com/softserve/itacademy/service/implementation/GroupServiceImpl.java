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
    public void updateDisabled(Integer id, boolean disabled) {
        if (groupRepository.updateDisabled(id, disabled) == 0) {
            throw new NotFoundException();
        }
    }

    @Override
    public Group findById(Integer id) {
        return getById(id);
    }

    //TODO make private
    public Group getById(Integer id) {
        return groupRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<GroupResponse> findByOwner(Integer ownerId) {
        return groupRepository.findByOwnerId(ownerId).stream()
                .filter(group -> !group.getDisabled())
                .map(groupConverter::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GroupResponse> findByStudent(Integer studentId) {
        return groupRepository.findByStudentId(studentId).stream()
                .filter(response -> !response.getDisabled())
                .map(groupConverter::convertToDto)
                .collect(Collectors.toList());
    }
}
