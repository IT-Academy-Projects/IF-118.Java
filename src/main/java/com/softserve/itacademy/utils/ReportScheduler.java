package com.softserve.itacademy.utils;

import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportScheduler {

    private final StatisticService statisticService;
    private final GroupService groupService;
    private final GroupRepository groupRepository;

    public ReportScheduler(StatisticService statisticService, GroupService groupService, GroupRepository groupRepository) {
        this.statisticService = statisticService;
        this.groupService = groupService;
        this.groupRepository = groupRepository;
    }

    @Scheduled(cron = "0 0 4 * * ?")
    public void saveUserReports() {
        groupRepository.findAll()
                .forEach(group -> groupService.findAllGroupsAndUsersIds(group)
                    .forEach(id -> statisticService.create(group.getId(), id)));
        
    }
}
