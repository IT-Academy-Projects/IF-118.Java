package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

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

    @Query(value = "select a.* from assignment a " +
            "join groups_assignments ga on a.id = ga.assignment_id " +
            "join student_groups sg on sg.id = ga.group_id " +
            "where sg.owner_id= ?1", nativeQuery = true)
    List<Assignment> findAllByOwnerId(Integer id);

    @Query(value = "select * from assignment join groups_assignments ga on assignment.id = ga.assignment_id " +
            " where ga.group_id = ?1", nativeQuery = true)
    Set<Assignment> findAllByGroupId(Integer id);

}
