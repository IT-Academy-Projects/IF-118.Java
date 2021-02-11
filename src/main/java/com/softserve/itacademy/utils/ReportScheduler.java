package com.softserve.itacademy.utils;

import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserReportRepository;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ReportScheduler {

    private final StatisticService statisticService;
    private final GroupService groupService;
    private final GroupRepository groupRepository;
    private final UserReportRepository userReportRepository;

    public ReportScheduler(StatisticService statisticService, GroupService groupService,
                           GroupRepository groupRepository, UserReportRepository userReportRepository) {
        this.statisticService = statisticService;
        this.groupService = groupService;
        this.groupRepository = groupRepository;
        this.userReportRepository = userReportRepository;
    }

    @Transactional
    @Scheduled(cron = "0 50 12 * * ?")
    public void saveUserReports() {
        groupRepository.findAll()
                .forEach(group -> groupService.findAllUsers(group)
                        .forEach(user -> {
                            Integer groupId = group.getId();
                            Integer id = user.getId();
                            if (!user.getDisabled()) {
                                if (!userReportRepository.existsByGroupIdAndUserId(groupId, id)) {
                                    statisticService.create(groupId, id);
                                } else {
                                    if (userReportRepository.isUpdatable(groupId, id)) {
                                        statisticService.create(groupId, id);
                                    }
                                }
                            } else {
                                userReportRepository.deleteByGroupIdAndUserId(groupId, id);
                            }
                        }));

    }
}
