package com.softserve.itacademy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class CourseResponse {

    @JsonProperty("name")
    private String name;
    @JsonProperty("owner")
    private Integer ownerId;
    @JsonProperty("groups")
    private Set<Integer> groupIds;
    @JsonProperty("disabled")
    private Boolean disabled;

}
