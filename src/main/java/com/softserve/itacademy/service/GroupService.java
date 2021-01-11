package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;

import java.util.List;

public interface GroupService {
    GroupResponse create(GroupRequest groupRequest);
    List<GroupResponse> findAll();
    void updateDisabled(Integer id, boolean disabled);
    GroupResponse findById(Integer id);
    Group getById(Integer id);
}
