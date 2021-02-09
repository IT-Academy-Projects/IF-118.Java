package com.softserve.itacademy.controller;

import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import com.softserve.itacademy.security.principal.UserPrincipal;
import com.softserve.itacademy.security.perms.CommentReadPermission;
import com.softserve.itacademy.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@RequestBody CommentRequest commentRequest,
                                                  @AuthenticationPrincipal UserPrincipal currentPrincipal) {
        commentRequest.setOwnerId(currentPrincipal.getId());
        return new ResponseEntity<>(commentService.create(commentRequest), HttpStatus.CREATED);
    }

    @CommentReadPermission
    @GetMapping("/material/{id}")
    public ResponseEntity<List<CommentResponse>> readByMaterialId(@PathVariable Integer id) {
        return new ResponseEntity<>(commentService.findByMaterial(id), HttpStatus.OK);
    }

//    @CommentReadPrivatePermission
//    @GetMapping("/material/{id}/private")
//    public ResponseEntity<List<CommentResponse>> readPrivateComments(@PathVariable Integer id, @AuthenticationPrincipal User user) {
//        return new ResponseEntity<>(commentService.findByMaterial(id, user), HttpStatus.OK);
//    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentResponse> readById(@PathVariable Integer id) {
        return new ResponseEntity<>(commentService.readById(id), HttpStatus.OK);
    }
}
