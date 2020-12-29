package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAll();

    @Modifying
    @Transactional
    @Query(value = "update users set users.name=:name, users.email=:email where users.id=:id", nativeQuery = true)
    int updateProfileInfo(@Param("id") Integer id, @Param("name") String name, @Param("email") String email);

    @Modifying
    @Transactional
    @Query(value = "update users set users.disabled = :disabled where users.id = :id", nativeQuery = true)
    int updateDisabled(Integer id, boolean disabled);

}
