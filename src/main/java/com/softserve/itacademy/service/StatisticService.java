package com.softserve.itacademy.service;

import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.UserStatisticResponse;

import java.util.Set;

public interface StatisticService {
    UserStatisticResponse getUserAnswersStatistic(Integer courseId, Integer userId);
    Set<AssignmentResponse> getAssignments(Integer courseId, Integer userId);
}
