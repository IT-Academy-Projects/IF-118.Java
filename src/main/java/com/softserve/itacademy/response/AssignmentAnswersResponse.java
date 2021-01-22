package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssignmentAnswersResponse {

    private Integer id;
    private Integer assignmentId;
    private Integer ownerId;
    private Integer grade;
}
