package com.softserve.itacademy.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserve.itacademy.entity.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@Builder
public class GroupRequest {

    @NotBlank
    @JsonProperty("name")
    private String name;

    @NotBlank
    @JsonProperty("owner")
    private Integer ownerId;

}
