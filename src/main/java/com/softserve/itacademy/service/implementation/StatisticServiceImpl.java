package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.repository.UserReportRepository;
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
    private final AssignmentRepository assignmentRepository;

    public StatisticServiceImpl(UserRepository userRepository, ReportService reportService,
                                AssignmentRepository assignmentRepository) {
        this.userRepository = userRepository;
        this.reportService = reportService;
        this.assignmentRepository = assignmentRepository;

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
        Set<Integer> allByGroup = assignmentRepository.findAllByGroup(groupId);

        Set<UserAssignmentResponse> assignments = getTinyStatistic(groupId, userId);
        if (assignments.isEmpty()) {
            return null;
        } else {
            double sum = assignments.stream()
                    .mapToDouble(UserAssignmentResponse::getGrade).sum();
            return String.format("%.2g%n", sum / allByGroup.size()).trim();
        }
    }

}