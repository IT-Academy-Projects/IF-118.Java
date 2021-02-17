package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query(value = "select * from material where id IN (:ids)", nativeQuery = true)
    Set<Material> findByIds(Set<Integer> ids);

    @Query(value = "select * from material where material.course_id IN (:ids)", nativeQuery = true)
    Set<Material> findByCourseIds(Set<Integer> ids);

    @Modifying
    @Query(value = "update groups_materials set opened = 1 where material_id = :materialId and group_id in (:groupIds)", nativeQuery = true)
    void openMaterial(Integer materialId, List<Integer> groupIds);

    @Modifying
    @Query(value = "update groups_materials set opened = 0 where material_id = :materialId and group_id =:groupId", nativeQuery = true)
    void closeMaterial(Integer materialId, Integer groupId);

    @Query(value = "select owner_id from material where id = :id", nativeQuery = true)
    int findOwnerIdById(Integer id);
}
