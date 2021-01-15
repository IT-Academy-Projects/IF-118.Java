package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.service.AssignmentService;
import com.softserve.itacademy.service.MaterialService;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.springframework.stereotype.Service;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    MaterialService materialService;
    AmazonS3ClientService amazonS3ClientService;
    AssignmentRepository assignmentRepository;
    AssignmentConverter assignmentConverter;

    public AssignmentServiceImpl(MaterialService materialService, AmazonS3ClientService amazonS3ClientService,
                                 AssignmentRepository assignmentRepository, AssignmentConverter assignmentConverter) {
        this.materialService = materialService;
        this.amazonS3ClientService = amazonS3ClientService;
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
        return assignmentRepository.findById(id).orElseThrow(NotFoundException::new);
    }

}
