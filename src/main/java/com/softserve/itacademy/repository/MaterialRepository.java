package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query(value = "select * from material where id IN (:ids)", nativeQuery = true)
    Set<Material> findByIds(Set<Integer> ids);

    @Query(value = "select group_id from material_expirations where opened = 0 and expiration_date < now() + interval 1 day", nativeQuery = true)
    List<Integer> findAllDueDateTimeExpiring();

    @Query(value = "select id from material where material.course_id IN (:ids)", nativeQuery = true)
    Set<Integer> findByCourseIds(Set<Integer> ids);

//    @Modifying
//    @Query(value = "INSERT INTO material_expirations (material_id, group_id, opened) values (:materialId, :groupId, 1); ", nativeQuery = true)
//    void saveMaterialsGroups(Integer materialId, Integer groupId);
}
