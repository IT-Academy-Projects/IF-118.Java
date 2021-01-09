package com.softserve.itacademy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ownerId")
    private Integer ownerId;

    @JsonProperty("disabled")
    private Boolean disabled;

    private Set<CourseResponse> courses;

    private Set<UserResponse> users;
}
