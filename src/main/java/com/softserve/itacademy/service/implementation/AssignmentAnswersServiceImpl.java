package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.FileHasNoExtensionException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentAnswersRepository;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.response.AssignmentAnswersResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.service.AssignmentAnswersService;
import com.softserve.itacademy.service.AssignmentService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.AssignmentAnswersConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class AssignmentAnswersServiceImpl implements AssignmentAnswersService {

    AssignmentService assignmentService;
    AssignmentAnswersRepository assignmentAnswersRepository;
    AssignmentAnswersConverter assignmentAnswersConverter;
    AmazonS3ClientService amazonS3ClientService;
    UserService userService;

    public AssignmentAnswersServiceImpl(AssignmentService assignmentService,
                                        AssignmentAnswersRepository assignmentAnswersRepository,
                                        AssignmentAnswersConverter assignmentAnswersConverter,
                                        AmazonS3ClientService amazonS3ClientService,
                                        UserService userService) {
        this.assignmentService = assignmentService;
        this.assignmentAnswersRepository = assignmentAnswersRepository;
        this.assignmentAnswersConverter = assignmentAnswersConverter;
        this.amazonS3ClientService = amazonS3ClientService;
        this.userService = userService;
    }

    @Override
    public AssignmentAnswersResponse findById(Integer id) {
        return assignmentAnswersConverter.of(getById(id));
    }

    @Override
    public AssignmentAnswersResponse create(MultipartFile file, AssignmentAnswersRequest assignmentAnswersRequest) {
        Assignment assignment = assignmentService.getById(assignmentAnswersRequest.getAssignmentId());
        if (userService.getById(assignmentAnswersRequest.getOwnerId()).getDisabled()) {
            throw new DisabledObjectException("User disabled");
        }
        AssignmentAnswers assignmentAnswers = AssignmentAnswers.builder()
                .ownerId(assignmentAnswersRequest.getOwnerId())
                .assignment(assignment)
                .fileReference(amazonS3ClientService.saveFile(file))
                .build();
        assignmentAnswers = assignmentAnswersRepository.save(assignmentAnswers);
        return assignmentAnswersConverter.of(assignmentAnswers);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        AssignmentAnswers assignmentAnswers = getById(id);
        String[] split = assignmentAnswers.getFileReference().split("\\.");
        if (split.length < 1) { throw new FileHasNoExtensionException("Wrong file format"); }
        String extension = split[split.length - 1];
        return DownloadFileResponse.builder()
                .file(amazonS3ClientService.downloadFile(assignmentAnswers.getFileReference()))
                .fileName(assignmentAnswers.getFileReference())
                .build();
    }

    @Override
    public AssignmentAnswers getById(Integer id) {
        return assignmentAnswersRepository.findById(id).orElseThrow(NotFoundException::new);
    }
}