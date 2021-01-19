package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer> {

    @Query(value = "select * from material where ID IN (:query)", nativeQuery = true)
    Set<Material> findByIds(String query);
}
