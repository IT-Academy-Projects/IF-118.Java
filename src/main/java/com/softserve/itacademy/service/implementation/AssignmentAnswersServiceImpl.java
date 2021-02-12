package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.entity.Event;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.DisabledObjectException;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.AssignmentAnswersRepository;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.repository.EventRepository;
import com.softserve.itacademy.repository.UserReportRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.response.AssignmentAnswersResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.service.AssignmentAnswersService;
import com.softserve.itacademy.service.AssignmentService;
import com.softserve.itacademy.service.EventService;
import com.softserve.itacademy.service.UserService;
import com.softserve.itacademy.service.converters.AssignmentAnswersConverter;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.softserve.itacademy.service.s3.S3Constants.ASSIGNMENTS_ANSWERS_FOLDER;
import static com.softserve.itacademy.service.s3.S3Constants.BUCKET_NAME;

@Service
public class AssignmentAnswersServiceImpl implements AssignmentAnswersService {

    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;
    private final AssignmentAnswersRepository assignmentAnswersRepository;
    private final AssignmentAnswersConverter assignmentAnswersConverter;
    private final AmazonS3ClientService amazonS3ClientService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final EventService eventService;
    private final EventRepository eventRepository;
    private final UserReportRepository userReportRepository;
    private static final String ANSWER_ID_NOT_FOUND = "Answer with such id not found";

    public AssignmentAnswersServiceImpl(AssignmentService assignmentService,
                                        AssignmentRepository assignmentRepository,
                                        AssignmentAnswersRepository assignmentAnswersRepository,
                                        AssignmentAnswersConverter assignmentAnswersConverter,
                                        AmazonS3ClientService amazonS3ClientService,
                                        UserService userService, UserRepository userRepository,
                                        EventService eventService, EventRepository eventRepository, UserReportRepository userReportRepository) {
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
        this.assignmentAnswersRepository = assignmentAnswersRepository;
        this.assignmentAnswersConverter = assignmentAnswersConverter;
        this.amazonS3ClientService = amazonS3ClientService;
        this.userService = userService;
        this.userRepository = userRepository;
        this.eventService = eventService;
        this.eventRepository = eventRepository;
        this.userReportRepository = userReportRepository;
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
                .fileReference(amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_ANSWERS_FOLDER, file))
                .status(AssignmentAnswers.AnswersStatus.NEW)
                .grade(0)
                .build();
        assignmentAnswers = assignmentAnswersRepository.save(assignmentAnswers);
        return assignmentAnswersConverter.of(assignmentAnswers);
    }

    @Override
    public DownloadFileResponse downloadById(Integer id) {
        AssignmentAnswers assignmentAnswers = getById(id);
        String userName = userRepository.findNameById(assignmentAnswers.getOwnerId());
        String assignmentName = assignmentAnswers.getAssignment().getName();
        String fileName = assignmentName + "_" + userName + "_answer" + "." + FilenameUtils.getExtension(assignmentAnswers.getFileReference());
        return DownloadFileResponse.builder()
                .file(amazonS3ClientService.download(BUCKET_NAME, assignmentAnswers.getFileReference()))
                .fileName(fileName)
                .build();
    }

    @Override
    public AssignmentAnswers getById(Integer id) {
        return assignmentAnswersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ANSWER_ID_NOT_FOUND));
    }

    @Override
    public void update(MultipartFile file, Integer id) {
        String oldFileRef = assignmentAnswersRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ANSWER_ID_NOT_FOUND))
                .getFileReference();
        amazonS3ClientService.delete(BUCKET_NAME, ASSIGNMENTS_ANSWERS_FOLDER, oldFileRef);
        String fileRef = amazonS3ClientService.upload(BUCKET_NAME, ASSIGNMENTS_ANSWERS_FOLDER, file);
        assignmentAnswersRepository.update(fileRef, id, AssignmentAnswers.AnswersStatus.NEW.name());
    }

    @Override
    public void submit(Integer id) {
        if (assignmentAnswersRepository.updateStatus(id, AssignmentAnswers.AnswersStatus.SUBMITTED.name()) == 0) {
            throw new NotFoundException(ANSWER_ID_NOT_FOUND);
        } else{
            createGradeOrRejectEvent(id, Event.EventType.SUBMIT_ANSWER);
        }
    }

    @Override
    public void reject(Integer id) {
        if (assignmentAnswersRepository.updateStatus(id, AssignmentAnswers.AnswersStatus.REJECTED.name()) == 0) {
            throw new NotFoundException(ANSWER_ID_NOT_FOUND);
        } else{
            createGradeOrRejectEvent(id, Event.EventType.REJECT_ANSWER);
        }
    }

    @Transactional
    @Override
    public void grade(Integer id, Integer grade) {
        if (assignmentAnswersRepository.updateGrade(id, grade) == 0) {
            throw new NotFoundException(ANSWER_ID_NOT_FOUND);
        } else {
            assignmentAnswersRepository.updateStatus(id, AssignmentAnswers.AnswersStatus.GRADED.name());
            int groupId = assignmentRepository.getGroupIdByAnswerId(id);
            int userId = assignmentAnswersRepository.findOwnerById(id);
            makeReportUpdatable(groupId, userId);
            createGradeOrRejectEvent(id, Event.EventType.GRADE_ANSWER);
        }
    }

    private void createGradeOrRejectEvent(Integer entityId, Event.EventType eventType) {
        Integer creatorId = assignmentAnswersRepository.findTeacherIdByAnswerId(entityId);
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new NotFoundException("User with id(" + creatorId + ") not found"));

        Integer recipientId = assignmentAnswersRepository.findOwnerById(entityId);
        List<User> recipients = userRepository.findById(recipientId).stream().collect(Collectors.toList());

        if (!recipients.isEmpty()) {
            Event event = Event.builder()
                    .creator(creator)
                    .recipients(recipients)
                    .type(eventType)
                    .entityId(entityId)
                    .build();

            eventService.sendNotificationFromEvent(eventRepository.save(event));
        }
    }

    @Override
    public void makeReportUpdatable(Integer groupId, Integer userId) {
        if (userReportRepository.existsByGroupIdAndUserId(groupId, userId)) {
            userReportRepository.makeUpdatable(groupId, userId);
        }
    }
}