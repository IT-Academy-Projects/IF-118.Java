package com.softserve.itacademy.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Set;


@Getter
@Setter
@Builder
public class GroupRequest {

    @NotBlank
    private String name;
    private Set<Integer> courseIds;

    @NotBlank
    private Integer ownerId;

}
