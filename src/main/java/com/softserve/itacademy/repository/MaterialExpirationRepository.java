package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.MaterialExpiration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialExpirationRepository extends JpaRepository<MaterialExpiration, Integer> {

    @Query(value = "select * from material_expirations where material_id = :materialId and group_id in (:groupIds)", nativeQuery = true)
    List<MaterialExpiration> getMaterialExpirations(Integer materialId, List<Integer> groupIds);

    @Modifying
    @Query(value = "update material_expirations as me set me.start_date = now(), me.expiration_date = :expirationDate, me.opened = 0 where me.material_id = :materialId and me.group_id in (:groupIds)", nativeQuery = true)
    void setMaterialExpiration(LocalDateTime expirationDate, Integer materialId, List<Integer> groupIds);
}
