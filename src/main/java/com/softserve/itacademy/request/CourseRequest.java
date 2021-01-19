package com.softserve.itacademy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
@Setter
@Builder
@ToString
public class CourseRequest {

    @NotBlank
    private String name;
    private String description;

    @NotBlank
    private Integer ownerId;
    private Set<Integer> materialIds;

}
