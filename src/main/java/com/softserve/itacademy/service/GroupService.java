package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface GroupService {
    GroupResponse create(GroupRequest groupRequest, MultipartFile file);

    List<GroupResponse> findAll();

    void updateDisabled(Integer id, boolean disabled);

    byte[] getAvatarById(Integer id);

    GroupResponse findById(Integer id);

    List<GroupResponse> findByOwner(Integer id);

    void updateGroup(Integer groupId, GroupRequest groupRequest);

    List<GroupResponse> findGroupsWithClosedMaterial(Integer materialId);

    void submitAssignment(Integer groupId, Integer assignmentId);

    Set<Integer> findAllUsersIds(Group group);
}
