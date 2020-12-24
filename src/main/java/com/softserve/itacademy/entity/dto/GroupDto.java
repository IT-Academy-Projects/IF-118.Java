package com.softserve.itacademy.entity.dto;

import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.entity.User;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupDto {

    private Integer id;
    private String name;
    private String ownerId;

    private GroupDto() {};

    public static GroupDto create(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.id = group.getId();
        groupDto.name = group.getName();
        return groupDto;
    }

}
