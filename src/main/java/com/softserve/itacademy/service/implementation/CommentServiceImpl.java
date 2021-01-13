package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Comment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.projection.UserFullTinyProjection;
import com.softserve.itacademy.repository.CommentRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import com.softserve.itacademy.service.CommentService;
import com.softserve.itacademy.service.converters.CommentConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentConverter commentConverter;
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    public CommentServiceImpl(CommentRepository commentRepository, CommentConverter commentConverter, MaterialRepository materialRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.commentConverter = commentConverter;
        this.materialRepository = materialRepository;
        this.userRepository = userRepository;
    }

    @Override
    public CommentResponse create(CommentRequest commentRequest) {
        log.info("Creating comment {}", commentRequest);
        Material material = materialRepository.findById(commentRequest.getMaterialId()).get();
        UserFullTinyProjection owner = userRepository.findProjectedById(commentRequest.getOwnerId()).get();

        Comment comment = commentConverter.of(commentRequest, material, owner);
        return null;
    }

    @Override
    public CommentResponse readById(Integer id) {
        return null;
    }

    @Override
    public CommentResponse update(CommentRequest commentRequest) {
        return null;
    }

    @Override
    public List<CommentResponse> findAll() {
        return null;
    }

    @Override
    public List<CommentResponse> findByOwner(Integer id) {
        return null;
    }

    @Override
    public Comment getById(Integer id) {
        return null;
    }
}
