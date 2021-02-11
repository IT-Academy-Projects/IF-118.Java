package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.service.AssignmentService;
import com.softserve.itacademy.service.GroupService;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import static com.softserve.itacademy.service.s3.S3Constants.ASSIGNMENTS_FOLDER;
import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private final AssignmentRepository assignmentRepository;
    private final AssignmentConverter assignmentConverter;
    private final AmazonS3ClientService amazonS3ClientService;
    private final GroupService groupService;

    private static final String ASSIGNMENT_ID_NOT_FOUND = "Assignment with such id was not found";
    private static final String ANSWER_ID_NOT_FOUND = "Answer with such id not found";

    public AssignmentServiceImpl(AssignmentRepository assignmentRepository, AssignmentConverter assignmentConverter,
                                 AmazonS3ClientService amazonS3ClientService, GroupService groupService) {
        this.assignmentRepository = assignmentRepository;
        this.assignmentConverter = assignmentConverter;
        this.amazonS3ClientService = amazonS3ClientService;
        this.groupService = groupService;
    }

    @Override
    public AssignmentResponse findById(Integer id) {
        return assignmentConverter.of(getById(id));
    }

    @Override
    public List<AssignmentResponse> findAllByOwnerId(Integer id) {
        return assignmentRepository.findAllByOwnerId(id).stream()
                .map(assignmentConverter::of)
                .collect(Collectors.toList());
    }

    public AssignmentResponse create(AssignmentRequest assignmentRequest, MultipartFile file) {
        Group group = groupService.getById(assignmentRequest.getGroupId());
        Assignment assignment = Assignment.builder()
                .name(assignmentRequest.getName())
                .description(assignmentRequest.getDescription())
                .groups(Set.of(group))
                .build();
        if (file != null) {
            assignment.setFileReference(amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_FOLDER, file));
        }
        group.getAssignments().add(assignment);
        assignment = assignmentRepository.save(assignment);
        return assignmentConverter.of(assignment);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        Assignment assignment = getById(id);
        String fileName = assignment.getName() + "." + FilenameUtils.getExtension(assignment.getFileReference());
        return DownloadFileResponse.builder()
                .file(amazonS3ClientService.download(BUCKET_NAME, assignment.getFileReference()))
                .fileName(fileName)
                .build();
    }

    @Override
    public void update(Integer id, AssignmentRequest assignmentRequest, MultipartFile file) {
        if (assignmentRepository.update(id, assignmentRequest.getName(), assignmentRequest.getDescription()) == 0) {
            throw new NotFoundException(ANSWER_ID_NOT_FOUND);
        }
        if (file != null) {
            String oldFileRef = assignmentRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException(ASSIGNMENT_ID_NOT_FOUND))
                    .getFileReference();
            amazonS3ClientService.delete(BUCKET_NAME, ASSIGNMENTS_FOLDER, oldFileRef);
            String fileRef = amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_FOLDER, file);
            assignmentRepository.updateFileRef(id, fileRef);
        }
    }

    @Override
    public void delete(Integer id) {
        Assignment assignment = getById(id);
        assignmentRepository.delete(assignment);
    }

    @Override
    public Assignment getById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ANSWER_ID_NOT_FOUND));
    }
}
