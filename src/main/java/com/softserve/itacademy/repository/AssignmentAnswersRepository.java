package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.AssignmentAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface AssignmentAnswersRepository extends JpaRepository<AssignmentAnswers, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set file_reference=:fileRef, status=:status where id=:id", nativeQuery = true)
    void update(String fileRef, Integer id, String status);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set status=:status where id=:id", nativeQuery = true)
    int updateStatus(Integer id, String status);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers " +
                   "set assignment_answers.grade = :grade " +
                   "where assignment_answers.id = :id", nativeQuery = true)
    int updateGrade(Integer id, Integer grade);

    @Query(value = "select * from assignment_answers aa where aa.owner_id = ?1 and aa.assignment_id = ?2", nativeQuery = true)
    Set<AssignmentAnswers> findByOwnerId(Integer ownerId, Integer assignmentId);

    Set<AssignmentAnswers> findByAssignmentId(Integer assignmentId);

    @Query(value = "select * from assignment_answers aa where aa.assignment_id = ?1 and aa.owner_id = ?2", nativeQuery = true)
    Optional<AssignmentAnswers> findByOwnerAndAssignment(Integer assignmentId, Integer ownerId);


    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set is_reviewed_by_teacher=true where id=:id", nativeQuery = true)
    Integer reviewByTeacher(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set is_student_saw_grade=true where id=:id", nativeQuery = true)
    Integer reviewByStudent(Integer id);

    @Query(value = "select * from assignment_answers join groups_assignments ga" +
            " on assignment_answers.assignment_id = ga.assignment_id where group_id = ?1", nativeQuery = true)
    Set<AssignmentAnswers> findAllByOwnerId(Integer id);
}
