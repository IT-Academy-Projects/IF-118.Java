package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.Authority;
import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

public interface CommentService {
    CommentResponse create(CommentRequest commentRequest);

    CommentResponse readById(Integer id);

    CommentResponse update(CommentRequest commentRequest);

    List<CommentResponse> findAll();

    List<CommentResponse> findByOwner(Integer id);

    List<CommentResponse> findByMaterial(Integer id);

    //List<CommentResponse> findByMaterial(Integer id, User currentUser);

}
