package com.softserve.itacademy.controller;

import static com.softserve.itacademy.config.Constance.API_V1;
import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.repository.UserReportRepository;
import com.softserve.itacademy.response.statistic.UserReportResponse;
import com.softserve.itacademy.service.ReportService;
import com.softserve.itacademy.service.StatisticService;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping(API_V1 + "statistic")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/{groupId}/{userId}")
    public ResponseEntity<UserReportResponse> getStat(@PathVariable Integer groupId, @PathVariable Integer userId) {
        UserReportResponse byId = reportService.getById(groupId, userId);
        return new ResponseEntity<>(byId, OK);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Set<UserReportResponse>> getGroupStatistic(@PathVariable Integer groupId) {
        Set<UserReportResponse> allByGroup = reportService.getAllByGroup(groupId);
        return new ResponseEntity<>(allByGroup, OK);
    }
}
