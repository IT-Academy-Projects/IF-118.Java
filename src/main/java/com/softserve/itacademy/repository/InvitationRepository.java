package com.softserve.itacademy.repository;

import com.softserve.itacademy.entity.Invitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    @Query("update Invitation inv set inv.approved = true where inv.id = ?1")
    Invitation update(Integer id);

    boolean existsByEmail(String email);

    @Query("update Invitation inv set inv.user.id = ?1")
    Invitation setUserId(Integer id);
}
