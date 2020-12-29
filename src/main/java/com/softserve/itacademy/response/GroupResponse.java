package com.softserve.itacademy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GroupResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("owner")
    private Integer ownerId;

    @JsonProperty("disabled")
    private Boolean disabled;
}
