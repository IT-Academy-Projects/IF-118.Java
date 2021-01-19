package com.softserve.itacademy.tools.security;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.entity.security.Role;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.tools.security.WithMockOwnUser;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Set;

public class WithMockOwnUserSecurityContextFactory implements WithSecurityContextFactory<WithMockOwnUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockOwnUser ownUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.builder()
                .name(ownUser.name())
                .password("password1")
                .activated(true)
                .disabled(false)
                .email("test@example.com")
                .roles(Set.of(
                        Role.builder().name("USER").build(),
                        Role.builder().name(ownUser.role()).build()))
                .isPickedRole(true)
                .build();

        Authentication auth = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
