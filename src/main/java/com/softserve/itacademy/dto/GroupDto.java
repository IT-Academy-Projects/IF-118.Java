package com.softserve.itacademy.dto;

import com.softserve.itacademy.entity.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class GroupDto {

    private Integer id;
    private String name;
    private String ownerId;
    private Boolean disabled;

    public static GroupDto create(Group group) {
        return GroupDto
                .builder()
                .id(group.getId())
                .name(group.getName())
                .disabled(group.getDisabled())
                .build();
    }

}
