package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.report.UserReport;
import com.softserve.itacademy.entity.report.UserReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, UserReportId> {

    @Query(value = "select u from UserReport u where u.groupId = ?1 and u.userId = ?2")
    Optional<UserReport> findById(Integer groupId, Integer userId);

    @Query(value = "select u from UserReport u where u.groupId = ?1")
    Set<UserReport> findAllByGroup(Integer groupId);

    @Modifying
    @Query(value = "delete from UserReport ur where ur.groupId = ?1 and  ur.userId = ?2")
    int deleteByGroupIdAndUserId(Integer groupId, Integer userId);

    @Modifying
    @Query(value = "update UserReport ur set ur.updatable = true where ur.groupId = ?1 and  ur.userId = ?2")
    int makeUpdatable(Integer groupId, Integer userId);

    @Query(value = "select ur.updatable from UserReport ur where ur.groupId = ?1 and  ur.userId = ?2")
    boolean isUpdatable(Integer groupId, Integer userId);

    boolean existsByGroupIdAndUserId(Integer groupId, Integer userId);
}
