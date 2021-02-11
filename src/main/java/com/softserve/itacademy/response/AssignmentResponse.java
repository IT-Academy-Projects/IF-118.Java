package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AssignmentResponse {

    private Integer id;
    private String name;
    private String description;
    private Integer materialId;
    Set<AssignmentAnswersResponse> assignmentAnswers;
}