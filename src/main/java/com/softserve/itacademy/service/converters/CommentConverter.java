package com.softserve.itacademy.service.converters;

import com.softserve.itacademy.entity.Comment;
import com.softserve.itacademy.entity.Material;
import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.request.CommentRequest;
import com.softserve.itacademy.response.CommentResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class CommentConverter {
    private final ModelMapper mapper;

    public CommentResponse of(Comment comment) {
        CommentResponse commentResponse = mapper.map(comment, CommentResponse.class);
        commentResponse.setCreated_at(comment.getCreatedAt().toString());
        commentResponse.setUpdated_at(comment.getUpdatedAt().toString());
        commentResponse.setOwnerId(comment.getOwner().getId());
        commentResponse.setMaterialId(comment.getMaterial().getId());
        return commentResponse;
    }

    public Comment of(CommentRequest commentRequest, Material material, User owner) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Comment comment = mapper.map(commentRequest, Comment.class);
        comment.setMaterial(material);
        comment.setOwner(owner);
        comment.setDisabled(false);
        return comment;
    }
}
