package com.softserve.itacademy.response.statistic;

import com.softserve.itacademy.response.AssignmentAnswersResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAnswerStatisticResponse extends UserTinyStaticResponse {
    private AssignmentAnswerStatisticResponse answer;
}
