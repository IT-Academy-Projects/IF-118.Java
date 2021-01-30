package com.softserve.itacademy.service;

import com.softserve.itacademy.response.statistic.GroupStatisticResponse;
import com.softserve.itacademy.response.statistic.UserFullStatisticResponse;

public interface StatisticService {
    UserFullStatisticResponse getUserAnswersStatistic(Integer courseId, Integer userId);
    GroupStatisticResponse getGroupStatistic(Integer groupId);
}
