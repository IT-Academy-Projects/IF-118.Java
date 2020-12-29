package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import org.springframework.stereotype.Component;

@Component
public class GroupConverter {

    public GroupResponse convertToDto(Group group) {
        return GroupResponse
                .builder()
                .id(group.getId())
                .name(group.getName())
                .ownerId(group.getOwnerId())
                .disabled(group.getDisabled())
                .build();
    }

    public Group convertToGroup(GroupRequest groupRequest) {
        return Group.builder()
                .name(groupRequest.getName())
                .ownerId(groupRequest.getOwnerId())
                .disabled(false)
                .build();
    }
}
