package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    @Modifying
    @Query(value = "update invitation set approved = true where user_id = ?1", nativeQuery = true)
    int update(Integer id);

    boolean existsByEmail(String email);

    @Modifying
    @Query("update Invitation inv set inv.user.id = ?1")
    int setUserId(Integer id);
}
