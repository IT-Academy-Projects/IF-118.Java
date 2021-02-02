package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.Assignment;
import com.softserve.itacademy.request.AssignmentRequest;
import com.softserve.itacademy.response.AssignmentResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentService {

    AssignmentResponse findById(Integer id);
    AssignmentResponse create(AssignmentRequest assignmentRequest, MultipartFile file);
    DownloadFileResponse downloadById(Integer id);
    void update(Integer id, AssignmentRequest assignmentRequest, MultipartFile file);
    void delete(Integer id);
    Assignment getById(Integer id);
}
