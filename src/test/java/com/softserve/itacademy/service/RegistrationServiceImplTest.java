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
import com.softserve.itacademy.service.implementation.RegistrationServiceImpl;
import com.softserve.itacademy.tools.security.WithMockOwnStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleService roleService;
    @Mock
    private MailDesignService mailDesignService;
    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .email("test@example.com")
                .name("tester")
                .activated(false)
                .pickedRole(false)
                .activationCode("testcode")
                .build();
        user.setId(1);
    }

    @Test
    void registerUserSuccess() {
        RegistrationRequest registrationRequest = getRegistrationRequest();

        SuccessRegistrationResponse exceptedResponse = SuccessRegistrationResponse.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .role(registrationRequest.getPickedRole())
                .build();

        User expectedUser = User.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .password("ENCODED_PASSWORD")
                .pickedRole(true)
                .build();

        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());

        SuccessRegistrationResponse actualResponse = registrationService.registerUser(registrationRequest);

        assertEquals(exceptedResponse, actualResponse);
        verify(userRepository, times(1)).save(expectedUser);
        verify(mailDesignService, times(1)).designAndQueue(eq(registrationRequest.getEmail()), anyString(), anyString());
        verify(passwordEncoder, times(1)).encode(registrationRequest.getPassword());
    }

    @Test
    void registerUserNotUniqueEmail() {
        RegistrationRequest registrationRequest = getRegistrationRequest();
        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.ofNullable(user));

        assertThrows(BadCredentialsException.class, () -> registrationService.registerUser(registrationRequest));
    }

    @Test
    void registerUserWithWrongRole() {
        RegistrationRequest registrationRequest = getRegistrationRequest();
        when(userRepository.findByActivationCode(registrationRequest.getEmail())).thenReturn(Optional.empty());
        registrationRequest.setPickedRole("ADMIN");

        assertThrows(BadCredentialsException.class, () -> registrationService.registerUser(registrationRequest));
    }

    @Test
    void activationSuccess() {
        when(userRepository.findByActivationCode(user.getActivationCode())).thenReturn(Optional.ofNullable(user));
        ActivationResponse expectedResponse = ActivationResponse.builder()
                .isActivated(true)
                .message("Successfully activated")
                .build();

        ActivationResponse actualResponse = registrationService.activateUser(user.getActivationCode());

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(user.setActivated(true));
        verify(userRepository, times(1)).findByActivationCode(eq(user.getActivationCode()));
    }

    @Test
    void activationInvalidCode() {
        String code = user.getActivationCode();
        when(userRepository.findByActivationCode(code)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> registrationService.activateUser(code));
    }

    @Test
    @WithMockOwnStudent
    void pickRoleSuccessTeacher() {
        Role teacherRole = Role.builder().name("TEACHER").build();
        Role userRole = Role.builder().name("USER").build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        when(roleService.getByNameIgnoreCase("TEACHER")).thenReturn(teacherRole);
        when(roleService.getByNameIgnoreCase("USER")).thenReturn(userRole);

        RolePickResponse expectedResponse = RolePickResponse.builder()
                .email(user.getEmail())
                .pickedRole("TEACHER")
                .build();

        RolePickResponse actualResponse = registrationService.pickRole(1, getRoleRequest());

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).save(user.setRoles(Set.of(userRole, teacherRole)));
    }

    @Test
    void pickRoleUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> registrationService.pickRole(1, getRoleRequest()));
    }

    @Test
    void pickRoleAlreadyPicked() {
        user.setPickedRole(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        assertThrows(RoleAlreadyPickedException.class, () -> registrationService.pickRole(1, getRoleRequest()));
    }

    private RegistrationRequest getRegistrationRequest() {
        return RegistrationRequest.builder()
                .email("test@example.com")
                .name("Test Tester")
                .password("password1")
                .pickedRole("TEACHER")
                .build();
    }

    private RolePickRequest getRoleRequest() {
        return RolePickRequest.builder().pickedRole("TEACHER").build();
    }
}
