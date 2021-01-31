package com.softserve.itacademy.response.statistic;

import com.softserve.itacademy.response.MaterialResponse;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MaterialStatisticResponse extends MaterialResponse {

    private Set<AssignmentStatisticResponse> assignmentStatisticResponses;

}
