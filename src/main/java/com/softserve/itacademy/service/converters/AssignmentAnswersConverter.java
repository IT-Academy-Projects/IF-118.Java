package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.response.AssignmentAnswersResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class AssignmentAnswersConverter {

    private final ModelMapper mapper;

    public AssignmentAnswersResponse of(AssignmentAnswers assignmentAnswers) {
        return mapper.map(assignmentAnswers, AssignmentAnswersResponse.class);
    }

    public AssignmentAnswers of(AssignmentAnswersRequest assignmentAnswersRequest) {
        return mapper.map(assignmentAnswersRequest, AssignmentAnswers.class);
    }
}
