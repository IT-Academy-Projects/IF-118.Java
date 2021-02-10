package com.softserve.itacademy.response.statistic.tech;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAssignmentResponse {

    private Integer assignmentId;
    private String name;
    private Integer answerId;
    private Integer grade;

}
