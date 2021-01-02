package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update courses set courses.disabled = :disabled where courses.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    List<Course> findByOwnerId(Integer ownerId);

    @Query(value = "select id, created_at, updated_at, name, owner_id, disabled from courses join users_courses uc on courses.id = uc.course_id where user_id = ?1", nativeQuery = true)
    List<Course> findByStudentId(Integer studentId);
}

