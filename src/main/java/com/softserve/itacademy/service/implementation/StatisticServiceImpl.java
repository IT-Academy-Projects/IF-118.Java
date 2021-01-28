package com.softserve.itacademy.service.implementation;

import static com.softserve.itacademy.config.Constance.USER_ID_NOT_FOUND;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentAnswersRepository;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.UserStatisticResponse;
import com.softserve.itacademy.service.StatisticService;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import com.softserve.itacademy.service.converters.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatisticServiceImpl implements StatisticService {

    private final UserConverter userConverter;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentAnswersRepository assignmentAnswersRepository;
    private final AssignmentConverter assignmentConverter;

    public StatisticServiceImpl(UserConverter userConverter, UserRepository userRepository,
                                AssignmentRepository assignmentRepository, AssignmentAnswersRepository assignmentAnswersRepository,
                                AssignmentConverter assignmentConverter) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentAnswersRepository = assignmentAnswersRepository;
        this.assignmentConverter = assignmentConverter;
    }

    @Override
    public UserStatisticResponse getUserAnswersStatistic(Integer courseId, Integer userId) {
        UserStatisticResponse userStatisticResponse = userConverter.statisticOf(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND)));
        userStatisticResponse.setAssignments(getAssignments(courseId, userId));

        return userStatisticResponse;
    }

    @Override
    public Set<AssignmentResponse> getAssignments(Integer courseId, Integer userId) {
        return assignmentRepository.findAllByCourse(courseId).stream()
                .map(assignment -> {
                    Set<AssignmentAnswers> answers = assignmentAnswersRepository.findByOwnerId(userId, assignment.getId());
                    if (!answers.isEmpty()) {
                        assignment.setAssignmentAnswers(answers);
                    }
                    else {
                        assignment.setAssignmentAnswers(null);
                    }
                return assignmentConverter.of(assignment); })
                .collect(Collectors.toSet());
    }
}
