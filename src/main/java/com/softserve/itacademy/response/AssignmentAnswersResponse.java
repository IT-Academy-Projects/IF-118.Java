package com.softserve.itacademy.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AssignmentAnswersResponse {

    private Integer id;
    private Integer assignmentId;
    private Integer ownerId;
    private Boolean isSubmitted;
    private Integer grade;
    private Boolean isReviewedByTeacher;
    private Boolean isStudentSawGrade;
}
