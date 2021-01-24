package com.softserve.itacademy.security.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.exception.RoleAlreadyPickedException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.RolePickRequest;
import com.softserve.itacademy.security.dto.RolePickResponse;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.RegistrationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.RoleService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${application.address}")
    private String address;

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    public RegistrationServiceImpl(RoleService roleService, UserRepository userRepository, PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
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
        sendActivationMessage(user);

        return SuccessRegistrationResponse.builder()
                .email(user.getEmail())
                .name(user.getName())
                .role(dto.getPickedRole())
                .build();
    }

    @Override
    public ActivationResponse activateUser(String code) {
        User user = userRepository.findByActivationCode(code).orElseThrow(() -> new NotFoundException("user was not found"));
        user.setActivated(true);
        userRepository.save(user);
        return ActivationResponse.builder()
                .isActivated(true)
                .message("Successfully activated")
                .build();
    }

    @Override
    public RolePickResponse pickRole(Integer userId, RolePickRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user was not found"));

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
        throw new RoleAlreadyPickedException("This account already picked a role");
    }

    private void setPickedRole(String role, User user) {

        if (!(role.equalsIgnoreCase("STUDENT") || role.equalsIgnoreCase("TEACHER"))) {
            throw new BadCredentialsException("Role not allowed");
        }

        Role userRole = roleService.findByNameIgnoreCase("USER");
        Role pickedRole = roleService.findByNameIgnoreCase(role);

        user.addRole(userRole);
        user.addRole(pickedRole);
        user.setIsPickedRole(true);
    }

    private void addUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is not unique");
        }

        userRepository.save(user);
    }

    private void sendActivationMessage(User user) {
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
}


