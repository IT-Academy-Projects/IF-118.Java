package com.softserve.itacademy.service;

import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;

import java.util.List;

public interface CommentService {
    CommentResponse create(CommentRequest commentRequest);

    CommentResponse readById(Integer id);

    CommentResponse update(CommentRequest commentRequest);

    List<CommentResponse> findAll();

    List<CommentResponse> findByOwner(Integer id);

    List<CommentResponse> findByMaterial(Integer id);

}
