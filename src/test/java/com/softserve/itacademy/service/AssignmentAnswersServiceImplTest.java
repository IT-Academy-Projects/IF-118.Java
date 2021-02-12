package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.AssignmentAnswersRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.service.converters.AssignmentAnswersConverter;
import com.softserve.itacademy.service.implementation.AssignmentAnswersServiceImpl;
import com.softserve.itacademy.service.s3.AmazonS3ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AssignmentAnswersServiceImplTest {
    @InjectMocks
    private AssignmentAnswersServiceImpl assignmentAnswersService;
    @Mock
    private AssignmentAnswersRepository assignmentAnswersRepository;
    @Mock
    private AssignmentService assignmentService;
    @Mock
    private UserService userService;
    @Mock
    private AmazonS3ClientService amazonS3ClientService;
    @Mock
    private AssignmentAnswersConverter assignmentAnswersConverter;
    @Mock
    private EventService eventService;
    @Mock
    private UserRepository userRepository;

    @Test
    void createNewAnswerTest() throws IOException {
        Path path = Paths.get("src/test/resources/text.txt");
        MultipartFile file = new MockMultipartFile("text.txt",
                "text.txt", "text/plain", Files.readAllBytes(path));
        AssignmentAnswers assignmentAnswers = buildAssignmentAnswerNew();
        when(assignmentAnswersRepository.save(any())).thenReturn(assignmentAnswers);
        when(userService.getById(anyInt())).thenReturn(buildUser());
        AssignmentAnswersRequest assignmentAnswersRequest = buildRequest();
        assignmentAnswersService.create(file, assignmentAnswersRequest);
        verify(assignmentAnswersRepository, times(1)).save(assignmentAnswers);
        verify(amazonS3ClientService, times(1)).upload(anyString(), anyString(), eq(file));
    }

    private AssignmentAnswers buildAssignmentAnswerNew() {
        return AssignmentAnswers.builder()
                .fileReference("ref")
                .ownerId(1)
                .assignment(new Assignment())
                .grade(0)
                .status(AssignmentAnswers.AnswersStatus.NEW)
                .build();
    }

    private AssignmentAnswersRequest buildRequest() {
        return AssignmentAnswersRequest.builder()
                .assignmentId(1)
                .ownerId(1)
                .build();
    }

    private User buildUser() {
        return User.builder()
                .name("name")
                .disabled(false)
                .build();
    }
}
