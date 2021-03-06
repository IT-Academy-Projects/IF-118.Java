package com.softserve.itacademy.service.implementation;

import static com.softserve.itacademy.config.Constance.ANSWER_ID_NOT_FOUND;
import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.service.AssignmentService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final MaterialService materialService;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentConverter assignmentConverter;

    public AssignmentServiceImpl(MaterialService materialService,
                                 AssignmentRepository assignmentRepository, AssignmentConverter assignmentConverter) {
        this.materialService = materialService;
        this.assignmentRepository = assignmentRepository;
        this.assignmentConverter = assignmentConverter;
    }

    @Override
    public AssignmentResponse findById(Integer id) {
        return assignmentConverter.of(getById(id));
    }

    @Override
    public AssignmentResponse create(AssignmentRequest assignmentRequest) {
        Material material = materialService.getById(assignmentRequest.getMaterialId());
        Assignment assignment = Assignment.builder()
                .name(assignmentRequest.getName())
                .description(assignmentRequest.getDescription())
                .material(material)
                .build();
        assignment = assignmentRepository.save(assignment);
        return assignmentConverter.of(assignment);
    }

    @Override
    public Assignment getById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ANSWER_ID_NOT_FOUND));
    }

}
