package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;

public interface AssignmentService {

    AssignmentResponse findById(Integer id);
    AssignmentResponse create(AssignmentRequest assignmentRequest);
    Assignment getById(Integer id);
}
