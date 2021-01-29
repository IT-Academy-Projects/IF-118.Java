package com.softserve.itacademy.entity.security;

import com.softserve.itacademy.entity.BasicEntity;
import com.softserve.itacademy.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class PasswordResetToken extends BasicEntity {

    public static final int EXPIRATION = 15;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Builder.Default
    private LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(EXPIRATION);
}