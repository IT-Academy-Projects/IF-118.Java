package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Integer id;
    private String name;
    private Integer ownerId;
    private Set<Integer> groupIds;
    private Set<Integer> materialIds;
    private Boolean disabled;
    private String description;
    private Integer imageId;

}
