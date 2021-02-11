package com.softserve.itacademy.security.ownauth;


import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.security.principal.UserPrincipal;
import com.softserve.itacademy.security.service.UserPrincipalService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountLockedException;


@Slf4j
public class OwnAuthProvider implements AuthenticationProvider {

    private final UserPrincipalService userPrincipalService;
    private final PasswordEncoder passwordEncoder;

    public OwnAuthProvider(UserPrincipalService userPrincipalService, PasswordEncoder passwordEncoder) {
        this.userPrincipalService = userPrincipalService;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userPrincipalService.getByEmail(email);

        if(user.getDisabled()) {
            throw new AccountLockedException("Account is disabled");
        }

        if(!user.getActivated()) {
            throw new AccountLockedException("Account is not activated");
        }

        log.debug("User " + email + " trying to authorize");

        if (passwordEncoder.matches(password, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(UserPrincipal.of(user), password, user.getAuthorities());
        } else {
            throw new BadCredentialsException("Wrong email or password");
        }

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
