package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.entity.Group;
import com.softserve.itacademy.repository.AssignmentRepository;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.service.converters.AssignmentConverter;
import com.softserve.itacademy.service.implementation.AssignmentServiceImpl;
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
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AssignmentServiceImplTest {
    @InjectMocks
    private AssignmentServiceImpl assignmentService;
    @Mock
    private AssignmentRepository assignmentRepository;
    @Mock
    private GroupService groupService;
    @Mock
    private AmazonS3ClientService amazonS3ClientService;
    @Mock
    private AssignmentConverter assignmentConverter;

    @Test
    void createAssignmentWithFileTest() throws IOException {
        Path path = Paths.get("src/test/resources/text.txt");
        MultipartFile file = new MockMultipartFile("text.txt",
                "text.txt", "text/plain", Files.readAllBytes(path));
        Assignment assignment = buildAssignment();
        when(groupService.getById(anyInt())).thenReturn(buildGroup());
        when(assignmentRepository.save(any())).thenReturn(assignment);
        AssignmentRequest assignmentRequest = buildRequest();
        assignmentService.create(assignmentRequest, file);
        verify(assignmentRepository, times(1)).save(assignment);
        verify(amazonS3ClientService, times(1)).upload(anyString(), anyString(), eq(file));
    }

    @Test
    void createAssignmentWithoutFileTest() {
        Assignment assignment = buildAssignment();
        when(groupService.getById(anyInt())).thenReturn(buildGroup());
        when(assignmentRepository.save(any())).thenReturn(assignment);
        AssignmentRequest assignmentRequest = buildRequest();
        assignmentService.create(assignmentRequest, null);
        verify(assignmentRepository, times(1)).save(assignment);
    }

    @Test
    void downloadTest() {
        Assignment assignment = buildAssignment();
        when(assignmentRepository.findById(anyInt())).thenReturn(java.util.Optional.of(assignment));
        assignmentService.downloadById(1);
        verify(amazonS3ClientService, times(1))
                .download(anyString(), eq(assignment.getFileReference()));
    }

    @Test
    void deleteTest() {
        Assignment assignment = buildAssignment();
        when(assignmentRepository.findById(anyInt())).thenReturn(java.util.Optional.of(assignment));
        assignmentService.delete(1);
        verify(amazonS3ClientService, times(1)).delete(anyString(), anyString(), eq(assignment.getFileReference()));
        verify(assignmentRepository, times(1)).delete(assignment);
    }

    private Assignment buildAssignment() {
        return Assignment.builder()
                .name("task")
                .description("do smth")
                .fileReference("ref")
                .assignmentAnswers(Collections.emptySet())
                .groups(Collections.emptySet())
                .build();
    }

    private AssignmentRequest buildRequest() {
        return AssignmentRequest.builder()
                .name("new task")
                .description("do smth")
                .groupId(1)
                .build();
    }

    private Group buildGroup() {
        return Group.builder()
                .name("group")
                .assignments(new HashSet<>())
                .courses(Collections.emptySet())
                .materials(Collections.emptySet())
                .users(Collections.emptySet())
                .build();
    }
}
