package com.softserve.itacademy.service;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.implementation.RegistrationServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @BeforeEach
    public void setup() {
        when(userRepository.save(Mockito.any(User.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(passwordEncoder.encode(Mockito.anyString())).thenReturn("ENCODED_PASSWORD");
        when(roleService.findByNameIgnoreCase("TEACHER")).thenReturn(Role.builder().name("TEACHER").build());
        when(roleService.findByNameIgnoreCase("STUDENT")).thenReturn(Role.builder().name("STUDENT").build());
        when(roleService.findByNameIgnoreCase("USER")).thenReturn(Role.builder().name("USER").build());
        //    when(mailSender.send(registrationRequest.getEmail(), Mockito.any(), Mockito.any()));
        // when(roleService.findByNameIgnoreCase(Mockito.anyString())).thenThrow(new NotFoundException());
    }

    @Test
    void testRegisterUserSuccess() {
        SuccessRegistrationResponse res = SuccessRegistrationResponse.builder()
                .email(registrationRequest.getEmail())
                .name(registrationRequest.getName())
                .build();
        when(userRepository.findByEmail(registrationRequest.getEmail())).thenReturn(Optional.empty());

        SuccessRegistrationResponse res2 = registrationService.registerUser(registrationRequest);
        assertEquals(res.getEmail(), res2.getEmail());
        assertEquals(res.getName(), res2.getName());
    }
}
