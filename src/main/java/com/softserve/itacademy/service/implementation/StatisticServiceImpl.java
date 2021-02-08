package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.statistic.tech.UserAssignmentResponse;
import com.softserve.itacademy.response.statistic.tech.UserFullStatisticResponse;
import com.softserve.itacademy.service.ReportService;
import com.softserve.itacademy.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
public class StatisticServiceImpl implements StatisticService {

    private final UserRepository userRepository;
    private final ReportService reportService;

    public StatisticServiceImpl(UserRepository userRepository, ReportService reportService) {
        this.userRepository = userRepository;
        this.reportService = reportService;
    }

    @Override
    public UserReport create(Integer groupId, Integer userId) {
        UserFullStatisticResponse user = UserFullStatisticResponse.builder()
                .userId(userId)
                .groupId(groupId)
                .userAssignmentResponse(getTinyStatistic(groupId, userId))
                .avg(getAvg(groupId, userId))
                .build();
        return reportService.save(user);
    }

    private Set<UserAssignmentResponse> getTinyStatistic(Integer groupId, Integer userId) {
        return userRepository.getStat(groupId, userId);
    }

    private String getAvg(Integer groupId, Integer userId) {
        double avg = getTinyStatistic(groupId, userId).stream()
                .mapToDouble(user -> {
                    if (user.getAnswerId() != null) {
                        return user.getGrade();
                    } else
                        return 0.0;
                })
                .average().orElse(0.0);
        return String.format("%.2g%n", avg).trim();
    }


}