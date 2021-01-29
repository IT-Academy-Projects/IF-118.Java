package com.softserve.itacademy.repository.security;

import com.softserve.itacademy.entity.security.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {

    Optional<PasswordResetToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query(value = "delete from password_reset_token where expiration_date < NOW()", nativeQuery = true)
    int deleteExpiredTokens();

    @Transactional
    @Modifying
    @Query(value = "delete from password_reset_token where user_id = ?", nativeQuery = true)
    void deleteByUserId(Integer id);

    @Query(value = "select * from password_reset_token where user_id = ?", nativeQuery = true)
    Optional<PasswordResetToken> findByUserId(Integer id);
}
