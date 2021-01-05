package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update courses set courses.disabled = :disabled where courses.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    @Query(value = "select * from courses where owner_id=:id", nativeQuery = true)
    Optional<List<Course>> findByOwner(Integer id);

}
