package com.softserve.itacademy.security;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.projection.GroupFullTinyProjection;
import com.softserve.itacademy.repository.GroupRepository;
import org.springframework.transaction.annotation.Transactional;

public class Decider {

    private final GroupRepository groupRepository;

    public Decider(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public boolean checkIfGroupMember(Object principal, Integer groupId) {

        User user = (User) principal;

        GroupFullTinyProjection group = groupRepository.findProjectedById(groupId).orElseThrow(NotFoundException::new);

        for (var usr : group.getUsers()) {
            if(usr.getId().equals(user.getId())) {
                return true;
            }
        }
        return false;
    }
}
