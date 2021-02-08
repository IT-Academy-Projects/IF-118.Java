package com.softserve.itacademy.response.statistic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReportResponse {
    private Integer userId;
    private Integer groupId;
    private String assignments;
}
