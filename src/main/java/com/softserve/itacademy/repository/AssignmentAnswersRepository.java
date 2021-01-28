package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.AssignmentAnswers;
import io.swagger.models.auth.In;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface AssignmentAnswersRepository extends JpaRepository<AssignmentAnswers, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set file_reference=:fileRef, is_submitted=false where id=:id", nativeQuery = true)
    void update(String fileRef, Integer id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set is_submitted=true where id=:id", nativeQuery = true)
    Integer submit(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers " +
                   "set assignment_answers.grade = :grade " +
                   "where assignment_answers.id = :id", nativeQuery = true)
    int updateGrade(Integer id, Integer grade);

    @Query(value = "select * from assignment_answers aa where aa.owner_id = ?1 and aa.assignment_id = ?2", nativeQuery = true)
    Set<AssignmentAnswers> findByOwnerId(Integer ownerId, Integer assignmentId);
}
