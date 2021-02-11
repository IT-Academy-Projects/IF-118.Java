package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.AssignmentAnswers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query(value = "select sg.owner_id from student_groups sg " +
                   "join groups_assignments ga on sg.id = ga.group_id " +
                   "join assignment a on ga.assignment_id = a.id " +
                   "join assignment_answers answ on a.id = answ.assignment_id " +
                   "where answ.id = :answerId", nativeQuery = true)
    int findTeacherIdByAnswerId(Integer answerId);

    @Query(value = "select aa.owner_id from assignment_answers aa " +
            "where aa.id = ?1", nativeQuery = true)
    int getOwnerId(Integer answerId);
}
