package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update student_groups as sg set sg.disabled = :disabled where sg.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    List<Group> findByOwnerId(Integer ownerId);

    @Query(value = "select id, created_at, updated_at, name, owner_id, disabled from student_groups join groups_users gu on student_groups.id = gu.group_id where user_id = ?1",nativeQuery = true)
    List<Group> findByStudentId(Integer studentId);

}
