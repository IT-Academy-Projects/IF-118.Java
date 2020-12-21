package com.ifjava.eduproject.repository;

import com.ifjava.eduproject.entity.Admin;
import com.ifjava.eduproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
