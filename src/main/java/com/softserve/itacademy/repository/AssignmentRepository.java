package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update assignment set name=:name, description=:description where id=:id", nativeQuery = true)
    int update(Integer id, String name, String description);

    @Modifying
    @Transactional
    @Query(value = "update assignment set file_reference=:fileRef where id=:id", nativeQuery = true)
    int updateFileRef(Integer id, String fileRef);

    @Query(value = "select assignment.* from assignment " +
            "join student_groups sg on assignment.group_id = sg.id " +
            "where sg.owner_id = :id", nativeQuery = true)
    List<Assignment> findAllByOwnerId(Integer id);
}
