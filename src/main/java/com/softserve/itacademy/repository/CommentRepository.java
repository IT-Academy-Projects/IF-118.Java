package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByOwnerId(Integer id);

    @Query(value = "select * from comments where material_id=:id and is_private=0 order by created_at desc", nativeQuery = true)
    List<Comment> findByMaterialId(Integer id);

//    In development
//    @Query(value = "select * from comments where material_id=:materialId and is_private=1 order by owner_id, created_at desc", nativeQuery = true)
//    List<Comment> findByMaterialIdPrivate(Integer materialId);
//
//    @Query(value = "select * from comments where material_id=:materialId and owner_id=:ownerId and is_private=1 order by created_at desc", nativeQuery = true)
//    List<Comment> findByMaterialIdStudentPrivate(Integer materialId, Integer ownerId);
}
