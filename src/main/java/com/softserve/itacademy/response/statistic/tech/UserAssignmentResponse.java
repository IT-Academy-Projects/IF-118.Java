package com.softserve.itacademy.response.statistic.tech;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAssignmentResponse {

    private Integer id;
    private String name;
    private Integer answerId;
    private Integer grade;

}
