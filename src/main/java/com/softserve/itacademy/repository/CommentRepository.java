package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByOwnerId(Integer id);
}
