package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.response.AssignmentAnswersResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.roles.StudentRolePermission;
import com.softserve.itacademy.security.perms.roles.UserRolePermission;
import com.softserve.itacademy.service.AssignmentAnswersService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/v1/assignment-answers")
public class AssignmentAnswersController {

    private final AssignmentAnswersService assignmentAnswersService;
    private final ObjectMapper objectMapper;

    public AssignmentAnswersController(AssignmentAnswersService assignmentAnswersService, ObjectMapper objectMapper) {
        this.assignmentAnswersService = assignmentAnswersService;
        this.objectMapper = objectMapper;
    }

    @StudentRolePermission
    @PostMapping
    public ResponseEntity<AssignmentAnswersResponse> create(@RequestPart(value = "file") MultipartFile file,
                                                            @RequestPart(value = "assignmentAnswer") String data,
                                                            @AuthenticationPrincipal User user) throws JsonProcessingException {
        AssignmentAnswersRequest assignmentAnswersRequest = objectMapper.readValue(data, AssignmentAnswersRequest.class);
        assignmentAnswersRequest.setOwnerId(user.getId());
        return new ResponseEntity<>(assignmentAnswersService.create(file, assignmentAnswersRequest), HttpStatus.CREATED);
    }

    @StudentRolePermission
    @PatchMapping
    public ResponseEntity<AssignmentAnswersResponse> update(@RequestPart(value = "file") MultipartFile file,
                                                            @RequestPart(value = "answerId") String id) throws JsonProcessingException {
        assignmentAnswersService.update(file, Integer.valueOf(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @UserRolePermission
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadById(@PathVariable Integer id) {
        DownloadFileResponse downloadFileResponse = assignmentAnswersService.downloadById(id);
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(downloadFileResponse.getFileName(), StandardCharsets.UTF_8)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentDisposition(contentDisposition);
        return ResponseEntity.ok().contentType(MediaType.MULTIPART_FORM_DATA)
                .headers(headers)
                .body(downloadFileResponse.getFile());
    }

    @CourseReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentAnswersResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentAnswersService.findById(id), HttpStatus.OK);
    }
}
