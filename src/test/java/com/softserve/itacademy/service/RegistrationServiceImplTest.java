package com.softserve.itacademy.service;

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
import com.softserve.itacademy.security.service.implementation.RegistrationServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.hibernate.annotations.NotFound;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    RoleService roleService;

    @Mock
    MailSender mailSender;

    @Mock
    UserRepository userRepository;

    RegistrationRequest registrationRequest = RegistrationRequest.builder()
            .email("test@example.com")
            .name("Test Tester")
            .password("password1")
            .pickedRole("TEACHER")
            .build();

    String pickedRole = "TEACHER";
    RolePickRequest roleRequest = RolePickRequest.builder().pickedRole(pickedRole).build();

    User user;

    @BeforeEach
    public void setup() {
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");
        when(roleService.findByNameIgnoreCase("TEACHER")).thenReturn(Role.builder().name("TEACHER").build());
        when(roleService.findByNameIgnoreCase("STUDENT")).thenReturn(Role.builder().name("STUDENT").build());
        when(roleService.findByNameIgnoreCase("USER")).thenReturn(Role.builder().name("USER").build());

        user = User.builder()
                .email("test@example.com")
                .name("tester")
                .activated(false)
                .isPickedRole(false)
                .activationCode("testcode")
                .build();
        user.setId(1);
    }

    @Test
    void testRegisterUserSuccess() {

        SuccessRegistrationResponse exceptedResponse = SuccessRegistrationResponse.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .role(registrationRequest.getPickedRole())
                .build();

        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());
        assertEquals(exceptedResponse, registrationService.registerUser(registrationRequest));
    }

    @Test
    void testRegisterUserNotUniqueEmail() {

        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.ofNullable(user));
        assertThrows(BadCredentialsException.class, () -> registrationService.registerUser(registrationRequest));
    }

    @Test
    void testRegisterUserWithWrongRole() {

        when(userRepository.findByActivationCode(registrationRequest.getEmail())).thenReturn(Optional.empty());
        registrationRequest.setPickedRole("ADMIN");
        assertThrows(BadCredentialsException.class, () -> registrationService.registerUser(registrationRequest));
    }

    @Test
    void testActivationSuccess() {

        when(userRepository.findByActivationCode(user.getActivationCode())).thenReturn(Optional.ofNullable(user));

        ActivationResponse expectedResponse = ActivationResponse.builder()
                .isActivated(true)
                .message("Successfully activated")
                .build();

        assertEquals(expectedResponse, registrationService.activateUser(user.getActivationCode()));
    }

    @Test
    void testActivationInvalidCode() {

        when(userRepository.findByActivationCode(user.getActivationCode())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> registrationService.activateUser(user.getActivationCode()));
    }

    @Test
    @WithMockUser // Not completely right thing to do, cuz we're using own auth principal.
    void pickRoleSuccessTeacher() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        RolePickResponse res = RolePickResponse.builder()
                .email(user.getEmail())
                .pickedRole(pickedRole)
                .build();

        assertEquals(res, registrationService.pickRole(1, roleRequest));
    }

    @Test
    void pickRoleUserNotFound() {

        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> registrationService.pickRole(1, roleRequest));
    }

    @Test
    void pickRoleAlreadyPicked() {

        user.setIsPickedRole(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        assertThrows(RoleAlreadyPickedException.class, () -> registrationService.pickRole(1, roleRequest));
    }
}
