package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {
    @Query(value = "SELECT * FROM assignment JOIN material m on assignment.material_id = m.id where m.course_id = ?1", nativeQuery = true)
    Set<Assignment> findAllByCourse(Integer courseId);
}
