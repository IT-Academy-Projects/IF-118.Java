package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.Comment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.CommentRepository;
import com.softserve.itacademy.repository.MaterialRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import com.softserve.itacademy.service.CommentService;
import com.softserve.itacademy.service.converters.CommentConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
        Material material = materialRepository.findById(commentRequest.getMaterialId())
                .orElseThrow(() -> new NotFoundException("Material with such id was not found"));
        User owner = userRepository.findById(commentRequest.getOwnerId())
                .orElseThrow(() -> new NotFoundException("User with such id was not found"));
        Comment comment = commentConverter.of(commentRequest, material, owner);
        Comment saved = commentRepository.save(comment);
        return commentConverter.of(saved);
    }

    @Override
    public CommentResponse readById(Integer id) {
        log.info("Searching for comment {}", id);
        return commentConverter.of(getById(id));
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
        log.info("Searching for comments of user {}", id);
        List<Comment> comments = commentRepository.findByOwnerId(id);
        if (comments == null) {
            log.info("No comments for user {}", id);
            return Collections.emptyList();
        }
        log.info("{} comments for user {} found", comments.size(), id);
        return collectComments(comments);
    }

    @Override
    public List<CommentResponse> findByMaterial(Integer id) {
        log.info("Searching for comments of material {}", id);
        List<Comment> comments = commentRepository.findByMaterialId(id);
        if (comments == null) {
            log.info("No comments for material {}", id);
            return Collections.emptyList();
        }
        log.info("{} comments for material {} found", comments.size(), id);
        return collectComments(comments);
    }

//    public List<CommentResponse> findByMaterial(Integer id, User currentUser) {
//        log.info("Searching for comments of material {}", id);
//        List<Comment> comments;
//        if (currentUser.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("comment.readAllPrivate"))) {
//            comments = commentRepository.findByMaterialIdStudentPrivate(id, currentUser.getId());
//        } else {
//            comments = commentRepository.findByMaterialIdPrivate(id);
//        }
//        if (comments == null) {
//            log.info("No comments for material {}", id);
//            return Collections.emptyList();
//        }
//        log.info("Comments: {}", comments);
//        return collectComments(comments);
//    }

    private Comment getById(Integer id) {
        return commentRepository.findById(id).orElseThrow(() -> new NotFoundException("Comment was not found"));
    }

    private List<CommentResponse> collectComments(List<Comment> comments) {
        return comments.stream()
                .map(commentConverter::of)
                .collect(Collectors.toList());
    }
}
