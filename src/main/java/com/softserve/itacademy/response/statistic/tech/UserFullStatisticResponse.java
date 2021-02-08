package com.softserve.itacademy.response.statistic.tech;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFullStatisticResponse {

    private Integer userId;

    private Integer groupId;

    private Set<UserAssignmentResponse> userAssignmentResponse;

    private String avg;
}
