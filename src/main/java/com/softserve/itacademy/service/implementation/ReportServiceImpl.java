package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserReportRepository;
import com.softserve.itacademy.response.statistic.UserReportResponse;
import com.softserve.itacademy.response.statistic.tech.UserFullStatisticResponse;
import com.softserve.itacademy.service.ReportService;
import com.softserve.itacademy.service.converters.UserReportConverter;
import org.springframework.stereotype.Service;

@Service
public class ReportServiceImpl implements ReportService {
    private final UserReportRepository userReportRepository;
    private final UserReportConverter userReportConverter;

    public ReportServiceImpl(UserReportRepository userReportRepository, UserReportConverter userReportConverter) {
        this.userReportRepository = userReportRepository;
        this.userReportConverter = userReportConverter;
    }

    @Override
    public UserReport save(UserFullStatisticResponse userFullStatisticResponse) {
        return userReportRepository.save(userReportConverter.of(userFullStatisticResponse));
    }

    @Override
    public UserReportResponse getById(Integer groupId, Integer userId) {
        return userReportConverter.of(userReportRepository.findById(groupId, userId).orElseThrow(() -> new NotFoundException("No such report")));
    }
}
