package com.softserve.itacademy.service;

import com.softserve.itacademy.dto.GroupDto;
import com.softserve.itacademy.entity.Group;

import java.util.List;

public interface GroupService {
    List<GroupDto> findAll();
    void delete(Integer id);
    void updateDisabled(Integer id, Boolean disabled);
    Group findById(Integer id);
}
