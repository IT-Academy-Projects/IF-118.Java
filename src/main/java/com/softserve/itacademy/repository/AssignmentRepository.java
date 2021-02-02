package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Query(value = "select assignment.* from assignment " +
            "join material on assignment.material_id = material.id " +
            "where material.owner_id = :id", nativeQuery = true)
    List<Assignment> findAllByOwnerId(Integer id);
}
