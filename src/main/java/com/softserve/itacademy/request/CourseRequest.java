package com.softserve.itacademy.request;

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
    private String name;

    @NotBlank
    private Integer ownerId;
    private Set<Integer> groupIds;

}
