package com.softserve.itacademy.service.implementation;

import static com.softserve.itacademy.config.Constance.GROUP_ID_NOT_FOUND;
import static com.softserve.itacademy.config.Constance.USER_ID_NOT_FOUND;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentAnswersRepository;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.repository.CourseRepository;
import com.softserve.itacademy.repository.GroupRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.MaterialResponse;
import com.softserve.itacademy.response.statistic.AssignmentAnswerStatisticResponse;
import com.softserve.itacademy.response.statistic.AssignmentStatisticResponse;
import com.softserve.itacademy.response.statistic.CourseStatisticResponse;
import com.softserve.itacademy.response.statistic.GroupStatisticResponse;
import com.softserve.itacademy.response.statistic.UserAnswerStatisticResponse;
import com.softserve.itacademy.response.statistic.UserFullStatisticResponse;
import com.softserve.itacademy.response.statistic.UserTinyStaticResponse;
import com.softserve.itacademy.service.StatisticService;
import com.softserve.itacademy.service.converters.AssignmentAnswersConverter;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import com.softserve.itacademy.service.converters.CourseConverter;
import com.softserve.itacademy.service.converters.GroupConverter;
import com.softserve.itacademy.service.converters.UserConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
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
    private final CourseRepository courseRepository;
    private final CourseConverter courseConverter;
    private final AssignmentAnswersConverter assignmentAnswersConverter;
    private final GroupConverter groupConverter;
    private final GroupRepository groupRepository;

    public StatisticServiceImpl(UserConverter userConverter, UserRepository userRepository,
                                AssignmentRepository assignmentRepository, AssignmentAnswersRepository assignmentAnswersRepository,
                                AssignmentConverter assignmentConverter, CourseRepository courseRepository,
                                CourseConverter courseConverter, AssignmentAnswersConverter assignmentAnswersConverter,
                                GroupConverter groupConverter, GroupRepository groupRepository) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
        this.assignmentAnswersRepository = assignmentAnswersRepository;
        this.assignmentConverter = assignmentConverter;
        this.courseRepository = courseRepository;
        this.courseConverter = courseConverter;
        this.assignmentAnswersConverter = assignmentAnswersConverter;
        this.groupConverter = groupConverter;
        this.groupRepository = groupRepository;
    }

    @Override
    public UserFullStatisticResponse getUserAnswersStatistic(Integer courseId, Integer userId) {
        UserFullStatisticResponse userStatisticResponse = userConverter.statisticOf(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND)));
        userStatisticResponse.setAssignments(getAssignmentsByUser(courseId, userId));
        return userStatisticResponse;
    }

    @Override
    public GroupStatisticResponse getGroupStatistic(Integer groupId) {
        GroupStatisticResponse groupStatisticResponse = buildGroupStat(groupId);
        Set<CourseStatisticResponse> courseStatisticResponses = getCourseStatisticResponses(groupId);

        courseStatisticResponses.forEach(courseStatisticResponse -> courseStatisticResponse.getMaterials()
                .forEach(materialResponse -> setAnswers(materialResponse, groupStatisticResponse)));

        groupStatisticResponse.setCourses(courseStatisticResponses);
        return groupStatisticResponse;
    }

    private GroupStatisticResponse buildGroupStat(Integer groupId) {
        Group group = groupRepository.findById(groupId).orElseThrow(() -> new NotFoundException(GROUP_ID_NOT_FOUND));
        return GroupStatisticResponse.builder()
                .users(group.getUsers().stream()
                        .map(userConverter::tinyStaticOf)
                        .collect(Collectors.toSet()))
                .build();
    }

    private void setAnswers(MaterialResponse materialResponse, GroupStatisticResponse groupStatisticResponse) {

        materialResponse.getAssignments()
                .forEach(assignmentResponse ->
                        assignmentResponse.setUsers(getUserAnswers(assignmentResponse, groupStatisticResponse))
                );
    }


    private Set<UserAnswerStatisticResponse> getUserAnswers(AssignmentStatisticResponse assignmentResponse, GroupStatisticResponse groupResponse) {
        Set<UserAnswerStatisticResponse> users = new HashSet<>();
        for (UserTinyStaticResponse user : groupResponse.getUsers()) {
            UserAnswerStatisticResponse userAnswerStatisticResponse = buildUserAnswerResponse(user);
            AssignmentAnswerStatisticResponse answer = assignmentAnswersConverter.statisticOf(assignmentAnswersRepository
                    .findByOwnerAndAssignment(assignmentResponse.getId(), user.getId()).orElse(null));
            userAnswerStatisticResponse.setAnswer(answer);
            users.add(userAnswerStatisticResponse);
        }

        return users;
    }

    private UserAnswerStatisticResponse buildUserAnswerResponse(UserTinyStaticResponse user) {
        UserAnswerStatisticResponse userAnswerStatisticResponse = new UserAnswerStatisticResponse();
        userAnswerStatisticResponse.setId(user.getId());
        userAnswerStatisticResponse.setName(user.getName());
        return userAnswerStatisticResponse;
    }

    private Set<CourseStatisticResponse> getCourseStatisticResponses(Integer groupId) {
        return courseRepository.findCoursesByGroups(groupId).stream()
                .map(courseConverter::statisticOf)
                .collect(Collectors.toSet());
    }

    private Set<AssignmentResponse> getAssignmentsByUser(Integer courseId, Integer userId) {
        return assignmentRepository.findAllByCourse(courseId).stream()
                .map(assignment -> {
                    Set<AssignmentAnswers> answers = assignmentAnswersRepository.findByOwnerId(userId, assignment.getId());
                    if (!answers.isEmpty()) {
                        assignment.setAssignmentAnswers(answers);
                    } else {
                        assignment.setAssignmentAnswers(Collections.emptySet());
                    }
                    return assignmentConverter.of(assignment);
                })
                .collect(Collectors.toSet());
    }
}