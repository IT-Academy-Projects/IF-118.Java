package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.report.UserReport;

public interface StatisticService {
    UserReport create(Integer groupId, Integer userId);
}
