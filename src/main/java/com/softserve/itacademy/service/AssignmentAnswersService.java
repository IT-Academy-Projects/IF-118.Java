package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.AssignmentAnswers;
import com.softserve.itacademy.request.AssignmentAnswersRequest;
import com.softserve.itacademy.response.AssignmentAnswersResponse;
import com.softserve.itacademy.response.DownloadFileResponse;
import org.springframework.web.multipart.MultipartFile;

public interface AssignmentAnswersService {

    AssignmentAnswersResponse findById(Integer id);
    AssignmentAnswersResponse create(MultipartFile file, AssignmentAnswersRequest assignmentAnswersRequest);
    DownloadFileResponse downloadById(Integer id);
    void grade(Integer id, Integer grade);
    AssignmentAnswers getById(Integer id);
    void update(MultipartFile file, Integer id);
    void submit(Integer id);
    void reject(Integer id);
}
