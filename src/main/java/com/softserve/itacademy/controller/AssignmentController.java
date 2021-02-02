package com.softserve.itacademy.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import com.softserve.itacademy.security.perms.CourseDeletePermission;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.CourseUpdatePermission;
import com.softserve.itacademy.security.perms.roles.TeacherRolePermission;
import com.softserve.itacademy.service.AssignmentService;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

import static com.softserve.itacademy.config.Constance.API_V1;

@RestController
@RequestMapping(API_V1 + "assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;
    private final ObjectMapper objectMapper;

    public AssignmentController(AssignmentService assignmentService, ObjectMapper objectMapper) {
        this.assignmentService = assignmentService;
        this.objectMapper = objectMapper;
    }

    @TeacherRolePermission
    @PostMapping
    public ResponseEntity<AssignmentResponse> create(@RequestPart(value = "file", required = false) MultipartFile file,
                                                     @RequestPart(value = "assignment") String data) throws JsonProcessingException {
        AssignmentRequest assignmentRequest = objectMapper.readValue(data, AssignmentRequest.class);
        return new ResponseEntity<>(assignmentService.create(assignmentRequest, file), HttpStatus.CREATED);
    }

    @CourseReadPermission
    @GetMapping("/{id}/file")
    public ResponseEntity<byte[]> downloadById(@PathVariable Integer id) {
        DownloadFileResponse downloadFileResponse = assignmentService.downloadById(id);
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
    public ResponseEntity<AssignmentResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentService.findById(id), HttpStatus.OK);
    }

    @CourseDeletePermission
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        assignmentService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CourseUpdatePermission
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id,
                                       @RequestPart(value = "file", required = false) MultipartFile file,
                                       @RequestPart(value = "assignment") String data) throws JsonProcessingException {
        AssignmentRequest assignmentRequest = objectMapper.readValue(data, AssignmentRequest.class);
        assignmentService.update(id, assignmentRequest, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
