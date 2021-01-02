package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.response.GroupResponse;

import java.util.List;

public interface GroupService {
    List<GroupResponse> findAll();
    void delete(Integer id);
    void updateDisabled(Integer id, boolean disabled);
    Group findById(Integer id);
    List<GroupResponse> findByOwner(Integer ownerId);
    List<GroupResponse> findByStudent(Integer studentId);
}
