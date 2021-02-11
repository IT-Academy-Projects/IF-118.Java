package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.PasswordResetToken;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.security.PasswordResetTokenRepository;
import com.softserve.itacademy.security.dto.PasswordByTokenRequest;
import com.softserve.itacademy.security.dto.ResetPasswordRequest;
import com.softserve.itacademy.service.implementation.PasswordResetServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class PasswordResetServiceImplTest {

    @InjectMocks
    PasswordResetServiceImpl passwordResetService;
    @Mock
    private UserService userService;
    @Mock
    private PasswordResetTokenRepository tokenRepository;
    @Mock
    private MailDesignService mailDesignService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .email("test@example.com")
                .name("tester")
                .activated(true)
                .pickedRole(false)
                .activationCode("testcode")
                .build();
        user.setId(1);
    }

    @Test
    void passwordResetSuccessNewToken() {
        when(userService.getByEmail(user.getEmail())).thenReturn(user);
        when(tokenRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResetPasswordRequest request = new ResetPasswordRequest(user.getEmail());

        passwordResetService.resetPassword(request);

        verify(tokenRepository, times(1)).findByUserId((user.getId()));
        verify(tokenRepository, times(1)).save(isA(PasswordResetToken.class));
        verify(mailDesignService, times(1)).designAndQueue(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void passwordResetSuccessTokenExists() {
        PasswordResetToken token = new PasswordResetToken("token", user, LocalDateTime.now().plusMinutes(5), false);
        when(userService.getByEmail(user.getEmail())).thenReturn(user);
        when(tokenRepository.findByUserId(user.getId())).thenReturn(Optional.of(token));
        when(tokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
        ResetPasswordRequest request = new ResetPasswordRequest(user.getEmail());

        passwordResetService.resetPassword(request);

        verify(tokenRepository, times(1)).findByUserId(user.getId());
        verify(tokenRepository, times(1)).save(isA(PasswordResetToken.class));
        verify(mailDesignService, times(1)).designAndQueue(eq(user.getEmail()), anyString(), anyString());
    }

    @Test
    void passwordResetEmailNotActivated() {
        user.setActivated(false);
        when(userService.getByEmail(user.getEmail())).thenReturn(user);
        ResetPasswordRequest request = new ResetPasswordRequest(user.getEmail());

        assertThrows(BadCredentialsException.class, () -> passwordResetService.resetPassword(request));
        verify(mailDesignService, times(0)).designAndQueue(eq(user.getEmail()), anyString(), anyString());
        verify(tokenRepository, times(0)).save(isA(PasswordResetToken.class));
    }

    @Test
    void setPasswordSuccess() {
        PasswordByTokenRequest request = new PasswordByTokenRequest("password1", "token");
        PasswordResetToken token = new PasswordResetToken("token", user, LocalDateTime.now().plusMinutes(5), false);
        when(userService.getUserByPasswordResetToken(token.getToken())).thenReturn(user);
        when(tokenRepository.findByToken(token.getToken())).thenReturn(Optional.of(token));

        passwordResetService.setPasswordByToken(request);

        verify(userService, times(1)).getUserByPasswordResetToken(request.getToken());
        verify(userService, times(1)).setPassword(user.getId(), request.getNewPassword());
        verify(tokenRepository, times(1)).setUsedByToken(request.getToken());
        verify(tokenRepository, times(1)).findByToken(request.getToken());
    }

    @Test
    void setPasswordExpiredToken() {
        PasswordByTokenRequest request = new PasswordByTokenRequest("password1", "token");
        PasswordResetToken token = new PasswordResetToken("token", user, LocalDateTime.now().minusMinutes(5), false);
        when(userService.getUserByPasswordResetToken(request.getToken())).thenReturn(user);
        when(tokenRepository.findByToken(request.getToken())).thenReturn(Optional.of(token));

        assertThrows(BadCredentialsException.class, () -> passwordResetService.setPasswordByToken(request));

        verify(userService, times(0)).setPassword(eq(user.getId()), eq(request.getNewPassword()));
    }

    @Test //
    void setPasswordUsedToken() {
        PasswordByTokenRequest request = new PasswordByTokenRequest("password1", "token");
        PasswordResetToken token = new PasswordResetToken("token", user, LocalDateTime.now().plusMinutes(5), true);
        when(userService.getUserByPasswordResetToken(request.getToken())).thenReturn(user);
        when(tokenRepository.findByToken(request.getToken())).thenReturn(Optional.of(token));

        assertThrows(BadCredentialsException.class, () -> passwordResetService.setPasswordByToken(request));

        verify(userService, times(0)).setPassword(eq(user.getId()), eq(request.getNewPassword()));
    }

    @Test //
    void setPasswordTokenNotFound() {
        PasswordByTokenRequest request = new PasswordByTokenRequest("password1", "token");
        when(userService.getUserByPasswordResetToken(request.getToken())).thenReturn(user);
        when(tokenRepository.findByToken(request.getToken())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> passwordResetService.setPasswordByToken(request));

        verify(userService, times(0)).setPassword(eq(user.getId()), eq(request.getNewPassword()));
    }
}
