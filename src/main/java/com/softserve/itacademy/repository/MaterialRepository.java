package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query(value = "select * from material where id IN (:ids)", nativeQuery = true)
    Set<Material> findByIds(Set<Integer> ids);

    @Query(value = "select group_id from materials_groups where is_opened = 0 and expiration_date < now() + interval 1 day", nativeQuery = true)
    List<Integer> findAllDueDateTimeExpiring();

    @Query(value = "select id from material where material.course_id IN (:ids)", nativeQuery = true)
    Set<Integer> findByCourseIds(Set<Integer> ids);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO materials_groups (material_id, group_id, is_opened) values (:materialId, :groupId, 1); ", nativeQuery = true)
    void saveMaterialsGroups(Integer materialId, Integer groupId);

    @Modifying
    @Transactional
    @Query(value = "update materials_groups as mg set mg.start_date = now(), mg.expiration_date = :expirationDate, mg.is_opened = 0 where mg.material_id = :materialId and group_id in (:groupIds)", nativeQuery = true)
    void setExpirationDate(LocalDateTime expirationDate, Integer materialId, List<Integer> groupIds);
}
