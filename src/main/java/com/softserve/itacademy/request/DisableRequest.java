package com.softserve.itacademy.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DisableRequest {

    @NotBlank
    @JsonProperty("disabled")
    private boolean disabled;

}
