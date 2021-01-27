package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.exception.DisabledObjectException;
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

import static com.softserve.itacademy.service.s3.S3Constants.ASSIGNMENTS_FOLDER;
import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;

@Service
public class AssignmentAnswersServiceImpl implements AssignmentAnswersService {

    private final AssignmentService assignmentService;
    private final AssignmentAnswersRepository assignmentAnswersRepository;
    private final AssignmentAnswersConverter assignmentAnswersConverter;
    private final AmazonS3ClientService amazonS3ClientService;
    private final UserService userService;

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
        Integer ownerId = assignmentAnswersRequest.getOwnerId();
        if (userService.getById(ownerId).getDisabled()) {
            throw new DisabledObjectException("Assignment answer can not be attached to disabled user " + ownerId);
        }
        AssignmentAnswers assignmentAnswers = AssignmentAnswers.builder()
                .ownerId(assignmentAnswersRequest.getOwnerId())
                .assignment(assignment)
                .fileReference(amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_FOLDER, file))
                .grade(0)
                .build();
        assignmentAnswers = assignmentAnswersRepository.save(assignmentAnswers);
        return assignmentAnswersConverter.of(assignmentAnswers);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        AssignmentAnswers assignmentAnswers = getById(id);
        return DownloadFileResponse.builder()
                .file(amazonS3ClientService.download(BUCKET_NAME, ASSIGNMENTS_FOLDER, assignmentAnswers.getFileReference()))
                .fileName(assignmentAnswers.getFileReference())
                .build();
    }

    @Override
    public void grade(Integer id, Integer grade) {
        if(assignmentAnswersRepository.updateGrade(id, grade) == 0){
            throw new NotFoundException("Assignment answer not found");
        }
    }

    @Override
    public AssignmentAnswers getById(Integer id) {
        return assignmentAnswersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment answer not found"));
    }

    @Override
    public void update(MultipartFile file, Integer id) {
        String oldFileRef = assignmentAnswersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Assignment answer " + id + " not found"))
                .getFileReference();
        amazonS3ClientService.delete(BUCKET_NAME, ASSIGNMENTS_FOLDER, oldFileRef);
        String fileRef = amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_FOLDER, file);
        assignmentAnswersRepository.update(fileRef, id);
    }

    @Override
    public void submit(Integer id) {
        if (assignmentAnswersRepository.submit(id) == 0) {
            throw new NotFoundException("Assignment answer " + id + " not found");
        }
    }
}