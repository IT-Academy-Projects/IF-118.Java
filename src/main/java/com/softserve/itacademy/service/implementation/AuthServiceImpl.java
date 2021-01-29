package com.softserve.itacademy.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.PasswordResetToken;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.RoleAlreadyPickedException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.repository.security.PasswordResetTokenRepository;
import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.PasswordByTokenRequest;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.ResetPasswordRequest;
import com.softserve.itacademy.security.dto.RolePickRequest;
import com.softserve.itacademy.security.dto.RolePickResponse;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.service.AuthService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.softserve.itacademy.config.Constance.USER_ID_NOT_FOUND;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {

    @Value("${application.address}")
    private String address;

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailSender mailSender;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserService userService;

    public AuthServiceImpl(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender, PasswordResetTokenRepository passwordResetTokenRepository, UserService userService) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public SuccessRegistrationResponse registerUser(RegistrationRequest dto) {

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .isPickedRole(false)
                .activationCode(UUID.randomUUID().toString())
                .build();

        setPickedRole(dto.getPickedRole(), user);
        addUser(user);
        sendActivationEmail(user);

        return SuccessRegistrationResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(dto.getPickedRole())
                .build();
    }

    @Override
    public ActivationResponse activateUser(String code) {
        User user = userRepository.findByActivationCode(code).orElseThrow(
                () -> new NotFoundException("User with " + code + " activation code was not found"));
        user.setActivated(true);
        userRepository.save(user);

        log.info("User with {} activation code successfully activated", code);

        return ActivationResponse.builder()
                .isActivated(true)
                .message("Successfully activated")
                .build();
    }

    @Override
    public RolePickResponse pickRole(Integer userId, RolePickRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND));

        if (!user.getIsPickedRole()) {

            setPickedRole(request.getPickedRole(), user);
            userRepository.save(user);

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            SecurityContextHolder.getContext().getAuthentication().getPrincipal(),
                            SecurityContextHolder.getContext().getAuthentication().getCredentials(),
                            user.getAuthorities())
            );

            return RolePickResponse.builder()
                    .email(user.getEmail())
                    .pickedRole(request.getPickedRole())
                    .build();
        }
        throw new RoleAlreadyPickedException("Account " + userId + " already picked a role");
    }

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {

        User user = userRepository.findByEmail(resetPasswordRequest.getEmail()).orElseThrow(() -> new NotFoundException(USER_ID_NOT_FOUND));

        //TODO: Check if token already exists

        if (!user.getActivated()) {
            throw new BadCredentialsException("Email is not confirmed");
        }

        String token = UUID.randomUUID().toString();

        sendPasswordResetEmail(createPasswordResetTokenForUser(user, token));
    }

    @Override
    public void setPasswordByToken(PasswordByTokenRequest dto) {
        if (validatePasswordResetToken(dto.getToken())) {
            User user = userService.getUserByPasswordResetToken(dto.getToken());
            userService.setPassword(user.getId(), dto.getNewPassword());
        }
    }


    private boolean validatePasswordResetToken(String token) {

        PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new NotFoundException("Token " + token + " not found"));
        if (passToken.getExpirationDate().isAfter(LocalDateTime.now())) {
            throw new BadCredentialsException("Token " + token + "has been expired");
        }
        return true;
    }

    private PasswordResetToken createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .build();
        return passwordResetTokenRepository.save(myToken);
    }

    private void setPickedRole(String role, User user) {

        if (!(role.equalsIgnoreCase("STUDENT") || role.equalsIgnoreCase("TEACHER"))) {
            throw new BadCredentialsException("User " + user.getId() + "picked forbidden registration role " + role);
        }

        Role userRole = roleService.getByNameIgnoreCase("USER");
        Role pickedRole = roleService.getByNameIgnoreCase(role);

        user.addRole(userRole);
        user.addRole(pickedRole);
        user.setIsPickedRole(true);
    }

    private void addUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is not unique");
        }

        log.info("User with {} email successfully registered", user.getEmail());

        userRepository.save(user);
    }

    private void sendActivationEmail(User user) {
        if (!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s! \n" + "Your activation link: %s/api/v1/activation/%s",
                    user.getName(),
                    address,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "SoftClass activation", message);
        }
    }

    private void sendPasswordResetEmail(PasswordResetToken token) {
        String message = String.format(
                "Hello, %s! \n" + "Your password reset link: %s/password-reset?token=%s",
                token.getUser().getName(),
                address,
                token.getToken()
        );

        mailSender.send(token.getUser().getEmail(), "SoftClass password reset", message);
    }
}



