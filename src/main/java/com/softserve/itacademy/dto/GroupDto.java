package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.Group;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class GroupDto {

    private Integer id;
    private String name;
    private String ownerId;
    private Boolean disabled;

    private GroupDto() {};

    public static GroupDto create(Group group) {
        GroupDto groupDto = new GroupDto();
        groupDto.id = group.getId();
        groupDto.name = group.getName();
        groupDto.disabled = group.getDisabled();
        return groupDto;
    }

}
