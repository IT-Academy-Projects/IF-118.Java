package com.softserve.itacademy.service;

import com.softserve.itacademy.security.dto.PasswordByTokenRequest;
import com.softserve.itacademy.security.dto.ResetPasswordRequest;
import org.springframework.transaction.annotation.Transactional;

public interface PasswordResetService {
    void resetPassword(ResetPasswordRequest resetPasswordRequest);

    @Transactional
    void setPasswordByToken(PasswordByTokenRequest dto);
}
