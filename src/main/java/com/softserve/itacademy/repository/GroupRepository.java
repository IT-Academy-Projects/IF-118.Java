package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Integer> {

    @Modifying
    @Transactional
    @Query(value = "update student_groups as sg set sg.disabled = :disabled where sg.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

    @Query(value = "select * from student_groups where owner_id = :id and disabled = false", nativeQuery = true)
    List<Group> findByOwnerId(Integer id);

    @Query(value = "select * from student_groups sg inner join groups_users gu on sg.id = gu.group_id where gu.user_id = :id and sg.disabled = false", nativeQuery = true)
    List<Group> findAllByUserId(Integer id);

    Optional<Group> findByIdAndOwnerId(Integer groupId, Integer ownerId);

    @Query(value = "select avatar from student_groups where id = :id", nativeQuery = true)
    byte[] getAvatarById(Integer id);

    Optional<Group> findByChatRoomId(Integer id);
}
