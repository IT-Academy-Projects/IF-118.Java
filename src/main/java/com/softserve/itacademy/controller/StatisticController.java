package com.softserve.itacademy.controller;

import static com.softserve.itacademy.config.Constance.API_V1;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.response.UserStatisticResponse;
import com.softserve.itacademy.service.StatisticService;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(API_V1 + "statistic")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<UserStatisticResponse> getUserStatistic(@PathVariable Integer courseId,
                                                                  @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(statisticService.getUserAnswersStatistic(courseId, user.getId()), OK);
    }
}
