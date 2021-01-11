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
public class GroupResponse {

    private Integer id;
    private String name;
    private Integer ownerId;
    private Boolean disabled;

    private Set<CourseResponse> courses;

    private Set<UserResponse> users;
}
