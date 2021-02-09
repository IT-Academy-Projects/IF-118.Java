package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.PasswordResetToken;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.security.PasswordResetTokenRepository;
import com.softserve.itacademy.security.dto.PasswordByTokenRequest;
import com.softserve.itacademy.security.dto.ResetPasswordRequest;
import com.softserve.itacademy.service.MailDesignService;
import com.softserve.itacademy.service.PasswordResetService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Value("${application.address}")
    private String address;

    private final UserService userService;
    private final PasswordResetTokenRepository tokenRepository;
    private final MailDesignService mailDesignService;

    public PasswordResetServiceImpl(UserService userService, PasswordResetTokenRepository tokenRepository,
            MailDesignService mailDesignService) {
        this.userService = userService;
        this.tokenRepository = tokenRepository;
        this.mailDesignService = mailDesignService;
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        User user = userService.getByEmail(resetPasswordRequest.getEmail());
        if (!user.getActivated()) {
            throw new BadCredentialsException("Email is not confirmed");
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = getOrCreateToken(user, token);
        sendPasswordResetEmail(resetToken);
    }

    @Override
    @Transactional
    public void setPasswordByToken(PasswordByTokenRequest dto) {
        if (validateToken(dto.getToken())) {
            User user = userService.getUserByPasswordResetToken(dto.getToken());
            userService.setPassword(user.getId(), dto.getNewPassword());
            tokenRepository.setUsedByToken(dto.getToken());
        }
    }

    @Override
    public int deleteExpiredTokens() {
        return tokenRepository.deleteExpiredTokens();
    }

    private boolean validateToken(String token) {

        PasswordResetToken passToken = tokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token " + token + " not found"));

        if (passToken.getExpirationDate().isBefore(LocalDateTime.now())) {
            throw new BadCredentialsException("Token " + token + " has been expired");
        }

        if (passToken.getUsed()) {
            throw new BadCredentialsException("Token " + token + " already been used");
        }

        return true;
    }

    private PasswordResetToken getOrCreateToken(User user, String token) {

        PasswordResetToken resetToken = tokenRepository.findByUserId(user.getId()).orElse(createToken(user, token));

        if (resetToken.getUsed()) {
            resetToken = createToken(user, token);
        }

        resetToken.setExpirationDate(LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION));
        return tokenRepository.save(resetToken);
    }

    private PasswordResetToken createToken(User user, String token) {

        return PasswordResetToken.builder()
                .user(user)
                .token(token)
                .build();
    }

    private void sendPasswordResetEmail(PasswordResetToken token) {
        String message = String.format(
                "Hello, %s! \n" + "Your password reset link: %s/password-reset-new?token=%s",
                token.getUser().getName(),
                address,
                token.getToken()
        );

        mailDesignService.designAndQueue(token.getUser().getEmail(), "SoftClass password reset", message);
    }
}
