package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.request.GroupRequest;
import com.softserve.itacademy.response.GroupResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class GroupConverter {

    private final ModelMapper mapper;

    public GroupResponse of(Group group) {
        return mapper.map(group, GroupResponse.class);
    }

    public Group of(GroupRequest groupRequest) {
        Group map = mapper.map(groupRequest, Group.class);
        map.setDisabled(false);
        return map;
    }
}
