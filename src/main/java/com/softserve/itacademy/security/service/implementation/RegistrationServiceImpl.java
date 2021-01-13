package com.softserve.itacademy.security.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.InvitationRepository;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.RegistrationService;
import com.softserve.itacademy.service.InvitationService;
import com.softserve.itacademy.service.MailSender;
import com.softserve.itacademy.service.RoleService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailSender mailSender;

    public RegistrationServiceImpl(RoleService roleService, UserRepository userRepository,
                                   PasswordEncoder passwordEncoder, MailSender mailSender) {
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    @Transactional
    @Override
    public SuccessRegistrationResponse registerUser(RegistrationRequest dto) {

        if (!(dto.getPickedRole().equals("STUDENT") || dto.getPickedRole().equals("TEACHER"))) {
            throw new BadCredentialsException("You can't pick such role");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(roleService.findByName("USER"))
                .role(roleService.findByName(dto.getPickedRole()))
                .activationCode(UUID.randomUUID().toString()).build();

        addUser(user);

        sendMessage(user);

        return SuccessRegistrationResponse.builder().email(user.getEmail()).name(user.getName()).id(user.getId()).build();
    }

    private void addUser(User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new BadCredentialsException("Email is not unique");
        }

        userRepository.save(user);
    }

    private void sendMessage(User user) {
        //TODO: Front End for activation
        if (!user.getEmail().isBlank()) {
            String message = String.format(
                    "Hello, %s! \n" + "Your activation link: http://localhost:8080/api/v1/activation/%s",
                    user.getName(),
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "SoftClass activation", message);
        }
    }

    @Override
    public ActivationResponse activateUser(String code) {
        User user = userRepository.findByActivationCode(code)
                .orElseThrow(() -> new NotFoundException("User with such activation code was not found"));
        user.setActivated(true);
        userRepository.save(user);
        return ActivationResponse.builder().isActivated(true).message("Successfully activated").build();
    }
}



