package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {

    @Query(value = "select file from images where id = :id", nativeQuery = true)
    byte[] findFileById(Integer id);

}
