package com.softserve.itacademy.response.statistic;

import com.softserve.itacademy.response.statistic.tech.UserAssignmentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportResponse {
    private Integer userId;
    private Integer groupId;
    private Set<UserAssignmentResponse> assignments;
    private String avg;
}
