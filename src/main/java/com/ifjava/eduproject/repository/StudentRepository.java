package com.ifjava.eduproject.repository;

import com.ifjava.eduproject.entity.Admin;
import com.ifjava.eduproject.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
