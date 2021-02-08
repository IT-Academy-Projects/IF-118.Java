package com.softserve.itacademy.service.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.response.statistic.UserReportResponse;
import com.softserve.itacademy.response.statistic.tech.UserFullStatisticResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserReportConverter {
    private final ObjectMapper objectMapper;

    @SneakyThrows
    public UserReport of(UserFullStatisticResponse userFullStatisticResponse) {
        return UserReport.builder()
                .userId(userFullStatisticResponse.getUserId())
                .groupId(userFullStatisticResponse.getGroupId())
                .assignments(objectMapper.writeValueAsString(userFullStatisticResponse.getUserAssignmentResponse()))
                .build();

    }

    public UserReportResponse of(UserReport userReport) {

        return UserReportResponse.builder()
                .userId(userReport.getUserId())
                .groupId(userReport.getGroupId())
                .assignments(userReport.getAssignments())
                .build();
    }
}
