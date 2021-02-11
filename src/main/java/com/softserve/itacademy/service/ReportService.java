package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.response.statistic.UserReportResponse;
import com.softserve.itacademy.response.statistic.tech.UserFullStatisticResponse;

import java.util.Set;

public interface ReportService {
    UserReport save(UserFullStatisticResponse userFullStatisticResponse);

    UserReportResponse getById(Integer groupId, Integer userId);

    Set<UserReportResponse> getAllByGroup(Integer groupId);
}
