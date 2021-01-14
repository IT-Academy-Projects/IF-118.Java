package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByOwnerId(Integer id);

    @Query(value = "select * from comments where material_id=:id order by created_at", nativeQuery = true)
    List<Comment> findByMaterialId(Integer id);
}
