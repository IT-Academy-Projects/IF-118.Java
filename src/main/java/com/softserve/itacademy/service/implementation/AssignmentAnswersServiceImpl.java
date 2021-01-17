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
import com.softserve.itacademy.service.s3.S3Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import static com.softserve.itacademy.service.s3.S3Constants.ASSIGNMENTS_FOLDER;
import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import static com.softserve.itacademy.service.s3.S3Constants.MATERIALS_FOLDER;

@Service
public class AssignmentAnswersServiceImpl implements AssignmentAnswersService {

    AssignmentService assignmentService;
    AssignmentAnswersRepository assignmentAnswersRepository;
    AssignmentAnswersConverter assignmentAnswersConverter;
    S3Utils s3Utils;
    UserService userService;

    public AssignmentAnswersServiceImpl(AssignmentService assignmentService,
                                        AssignmentAnswersRepository assignmentAnswersRepository,
                                        AssignmentAnswersConverter assignmentAnswersConverter,
                                        S3Utils s3Utils,
                                        UserService userService) {
        this.assignmentService = assignmentService;
        this.assignmentAnswersRepository = assignmentAnswersRepository;
        this.assignmentAnswersConverter = assignmentAnswersConverter;
        this.s3Utils = s3Utils;
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
//            TODO if you decided to specify exception message so include some debug info there. "Assignment can not be attached to disabled user id 123"
            throw new DisabledObjectException("User disabled");
        }
        AssignmentAnswers assignmentAnswers = AssignmentAnswers.builder()
                .ownerId(assignmentAnswersRequest.getOwnerId())
                .assignment(assignment)
                .fileReference(s3Utils.saveFile(file, BUCKET_NAME, ASSIGNMENTS_FOLDER))
                .build();
        assignmentAnswers = assignmentAnswersRepository.save(assignmentAnswers);
        return assignmentAnswersConverter.of(assignmentAnswers);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        AssignmentAnswers assignmentAnswers = getById(id);
        String[] split = assignmentAnswers.getFileReference().split("\\.");
//TODO do you really need extension. What if I need to upload file without it?
        if (split.length < 1) {
            throw new FileHasNoExtensionException("Wrong file format");
        }
        return DownloadFileResponse.builder()
                .file(s3Utils.downloadFile(assignmentAnswers.getFileReference(), BUCKET_NAME, ASSIGNMENTS_FOLDER))
                .fileName(assignmentAnswers.getFileReference())
                .build();
    }

    @Override
    public AssignmentAnswers getById(Integer id) {
        return assignmentAnswersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment answer not found"));
    }
}