package com.softserve.itacademy.response.statistic;

import com.softserve.itacademy.response.AssignmentResponse;
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
public class UserFullStatisticResponse extends UserTinyStaticResponse {

    private Set<AssignmentResponse> assignments;
    private String avg;

}
