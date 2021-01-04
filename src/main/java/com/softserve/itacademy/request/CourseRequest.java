package com.softserve.itacademy.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Builder
public class CourseRequest {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("owner")
    private Integer ownerId;

//    TODO bk keep the same json field name as in your bean and remove this annotation. It's less confusing and more supportable
    @JsonProperty("groups")
    private Set<Integer> groupIds;

}
