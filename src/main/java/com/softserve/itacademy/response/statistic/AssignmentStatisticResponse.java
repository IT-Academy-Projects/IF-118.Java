package com.softserve.itacademy.response.statistic;

import com.softserve.itacademy.response.AssignmentAnswersResponse;
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
public class AssignmentStatisticResponse  extends AssignmentAnswersResponse {
    private Set<UserAnswerStatisticResponse> users;

}
