package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
