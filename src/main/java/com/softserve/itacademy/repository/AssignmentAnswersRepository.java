package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.AssignmentAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set is_reviewed_by_teacher=true where id=:id", nativeQuery = true)
    Integer reviewByTeacher(Integer id);

    @Modifying
    @Transactional
    @Query(value = "update assignment_answers set is_student_saw_grade=true where id=:id", nativeQuery = true)
    Integer reviewByStudent(Integer id);

    List<AssignmentAnswers> findAllByOwnerId(Integer id);
}
