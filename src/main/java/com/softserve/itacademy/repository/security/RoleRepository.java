package com.softserve.itacademy.repository.security;

import com.softserve.itacademy.entity.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query(value = "select * from role r where upper(r.name) = upper(?)" , nativeQuery = true)
    Optional<Role> findByNameIgnoreCase(String name);

}
