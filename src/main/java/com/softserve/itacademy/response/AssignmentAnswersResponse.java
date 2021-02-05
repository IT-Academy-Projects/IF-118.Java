package com.softserve.itacademy.response;

import com.softserve.itacademy.entity.AssignmentAnswers;
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
    private AssignmentAnswers.AnswersStatus status;
    private Integer grade;
    private Boolean isReviewedByTeacher;
    private Boolean isStudentSawGrade;
}
