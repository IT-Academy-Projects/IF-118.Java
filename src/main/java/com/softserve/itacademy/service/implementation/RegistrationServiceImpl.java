package com.softserve.itacademy.service.implementation;

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
import com.softserve.itacademy.service.MailDesignService;
import com.softserve.itacademy.service.RegistrationService;
import com.softserve.itacademy.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class RegistrationServiceImpl implements RegistrationService {

    @Value("${application.address}")
    private String address;

    private final RoleService roleService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailDesignService mailDesignService;

    public RegistrationServiceImpl(RoleService roleService, UserRepository userRepository,
                                   PasswordEncoder passwordEncoder, MailDesignService mailDesignService) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailDesignService = mailDesignService;
    }

    @Transactional
    @Override
    public SuccessRegistrationResponse registerUser(RegistrationRequest dto) {

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .pickedRole(false)
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
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id not found"));

        if (!user.getPickedRole()) {

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


    private void setPickedRole(String role, User user) {

        if (!(role.equalsIgnoreCase("STUDENT") || role.equalsIgnoreCase("TEACHER"))) {
            throw new BadCredentialsException("User " + user.getId() + "picked forbidden registration role " + role);
        }

        Role userRole = roleService.getByNameIgnoreCase("USER");
        Role pickedRole = roleService.getByNameIgnoreCase(role);

        user.addRole(userRole);
        user.addRole(pickedRole);
        user.setPickedRole(true);
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
            String activationLink = address + "/api/v1/activation/" + user.getActivationCode();
            Map<String, Object> mailContext = new HashMap<String, Object>();
            mailContext.put("name", user.getName());
            mailContext.put("link",  activationLink);

            mailDesignService.designAndQueue(user.getEmail(), "SoftClass activation",
                    mailContext, MailDesignServiceImpl.MailType.ACTIVATION);
        }
    }
}



