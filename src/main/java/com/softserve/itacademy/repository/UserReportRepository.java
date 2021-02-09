package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.entity.report.UserReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, UserReportId> {

    @Query(value = "select u from UserReport u where u.groupId = ?1 and u.userId = ?2")
    Optional<UserReport> findById(Integer groupId, Integer userId);

    @Query(value = "select u from UserReport u where u.groupId = ?1")
    Set<UserReport> findAllByGroup(Integer groupId);
}
