package com.softserve.itacademy.service.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.response.statistic.UserReportResponse;
import com.softserve.itacademy.response.statistic.tech.UserAssignmentResponse;
import com.softserve.itacademy.response.statistic.tech.UserFullStatisticResponse;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
                .avg(userFullStatisticResponse.getAvg())
                .updatable(false)
                .build();

    }

    @SneakyThrows
    public UserReportResponse of(UserReport userReport) {
        UserAssignmentResponse[] userAssignmentResponses = objectMapper.readValue(userReport.getAssignments(), UserAssignmentResponse[].class);
        Set<UserAssignmentResponse> set = new HashSet<>(Arrays.asList(userAssignmentResponses));
        return UserReportResponse.builder()
                .userId(userReport.getUserId())
                .groupId(userReport.getGroupId())
                .assignments(set)
                .avg(userReport.getAvg())
                .build();
    }
}
