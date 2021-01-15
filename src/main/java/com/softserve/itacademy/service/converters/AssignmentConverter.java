package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class AssignmentConverter {

    private final ModelMapper mapper;
    private final AssignmentAnswersConverter assignmentAnswersConverter;

    public AssignmentResponse of(Assignment assignment) {
        AssignmentResponse map = mapper.map(assignment, AssignmentResponse.class);
        map.setAssignmentAnswers(assignment.getAssignmentAnswers().stream()
                .map(assignmentAnswersConverter::of)
                .collect(Collectors.toSet()));
        return map;
    }

    public Assignment of(AssignmentRequest assignmentRequest) {
        return mapper.map(assignmentRequest, Assignment.class);
    }
}
