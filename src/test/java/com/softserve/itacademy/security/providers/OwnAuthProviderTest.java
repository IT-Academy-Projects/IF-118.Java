package com.softserve.itacademy.security.providers;


import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.security.ownauth.OwnAuthProvider;
import com.softserve.itacademy.security.principal.UserPrincipal;
import com.softserve.itacademy.security.service.UserPrincipalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.security.auth.login.AccountLockedException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OwnAuthProviderTest {

    @InjectMocks
    private OwnAuthProvider provider;
    @Mock
    private UserPrincipalService userPrincipalService;
    @Mock
    private PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .email("test@example.com")
                .password("password1")
                .name("tester")
                .activated(true)
                .pickedRole(false)
                .activationCode("testcode")
                .build();

        when(userPrincipalService.getByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(Mockito.anyString(), Mockito.anyString())).thenAnswer(
                (invocation) -> {
                    String pass = invocation.getArgument(0);
                    String pass2 = invocation.getArgument(1);
                    return pass.equals(pass2);
                }
        );
    }

    @Test
    void authenticateSuccess() {
        Authentication expectedResult = new UsernamePasswordAuthenticationToken(UserPrincipal.of(user), user.getPassword(), user.getAuthorities());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        Authentication actualResult = provider.authenticate(authentication);

        assertEquals(expectedResult, actualResult);
        verify(userPrincipalService, times(1)).getByEmail(eq(authentication.getName()));
        verify(passwordEncoder, times(1)).matches(anyString(), anyString());
    }

    @Test
    void authenticateWrongPass() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), "123");

        assertThrows(BadCredentialsException.class, () -> provider.authenticate(authentication));
    }


    @Test
    void authenticateNotActive() {
        user.setActivated(false);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        assertThrows(AccountLockedException.class, () -> provider.authenticate(authentication));
    }


    @Test
    void authenticateDisabled() {
        user.setDisabled(true);
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());

        assertThrows(AccountLockedException.class, () -> provider.authenticate(authentication));
    }

    @Test
    void authenticateNotFound() {
        when(userPrincipalService.getByEmail(Mockito.anyString())).thenThrow(new NotFoundException("User not found"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("wrong@example.com", user.getPassword());

        assertThrows(NotFoundException.class, () -> provider.authenticate(authentication));
    }

}
