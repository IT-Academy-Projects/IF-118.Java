package com.softserve.itacademy.controller;

import static com.softserve.itacademy.config.Constance.API_V1;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.security.perms.CourseReadPermission;
import com.softserve.itacademy.security.perms.roles.TeacherRolePermission;
import com.softserve.itacademy.service.AssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(API_V1 + "assignments")
public class AssignmentController {

    private final AssignmentService assignmentService;

    public AssignmentController(AssignmentService assignmentService) {
        this.assignmentService = assignmentService;
    }

    @TeacherRolePermission
    @PostMapping
    public ResponseEntity<AssignmentResponse> create(@RequestBody AssignmentRequest assignmentRequest) {
        return new ResponseEntity<>(assignmentService.create(assignmentRequest), HttpStatus.CREATED);
    }

    @CourseReadPermission
    @GetMapping("/{id}")
    public ResponseEntity<AssignmentResponse> findById(@PathVariable Integer id) {
        return new ResponseEntity<>(assignmentService.findById(id), HttpStatus.OK);
    }
}
