package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
