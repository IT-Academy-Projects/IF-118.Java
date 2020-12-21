package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "select * from users", nativeQuery = true)
    List<User> findAll();

}
