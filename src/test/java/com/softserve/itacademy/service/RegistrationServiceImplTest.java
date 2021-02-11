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
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    private RegistrationRequest registrationRequest;
    private String pickedRole = "TEACHER";
    private RolePickRequest roleRequest;
    private User user;

    @BeforeEach
    public void setup() {
        when(userRepository.save(Mockito.any(User.class))).thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");
        when(roleService.getByNameIgnoreCase("TEACHER")).thenReturn(Role.builder().name("TEACHER").build());
        when(roleService.getByNameIgnoreCase("STUDENT")).thenReturn(Role.builder().name("STUDENT").build());
        when(roleService.getByNameIgnoreCase("USER")).thenReturn(Role.builder().name("USER").build());

        user = User.builder()
                .email("test@example.com")
                .name("tester")
                .activated(false)
                .pickedRole(false)
                .activationCode("testcode")
                .build();
        user.setId(1);

        registrationRequest = RegistrationRequest.builder()
                .email("test@example.com")
                .name("Test Tester")
                .password("password1")
                .pickedRole("TEACHER")
                .build();

        roleRequest = RolePickRequest.builder().pickedRole(pickedRole).build();
    }

    @Test
    void testRegisterUserSuccess() {
        SuccessRegistrationResponse exceptedResponse = SuccessRegistrationResponse.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .role(registrationRequest.getPickedRole())
                .build();
        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());

        SuccessRegistrationResponse actualResponse = registrationService.registerUser(registrationRequest);

        assertEquals(exceptedResponse, actualResponse);
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

        ActivationResponse actualResponse = registrationService.activateUser(user.getActivationCode());

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void testActivationInvalidCode() {
        String code = user.getActivationCode();
        when(userRepository.findByActivationCode(code)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> registrationService.activateUser(code));
    }

    @Test
    @WithMockOwnStudent
    void pickRoleSuccessTeacher() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));
        RolePickResponse expectedResponse = RolePickResponse.builder()
                .email(user.getEmail())
                .pickedRole(pickedRole)
                .build();

        RolePickResponse actualResponse = registrationService.pickRole(1, roleRequest);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    void pickRoleUserNotFound() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> registrationService.pickRole(1, roleRequest));
    }

    @Test
    void pickRoleAlreadyPicked() {
        user.setPickedRole(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.ofNullable(user));

        assertThrows(RoleAlreadyPickedException.class, () -> registrationService.pickRole(1, roleRequest));
    }
}
