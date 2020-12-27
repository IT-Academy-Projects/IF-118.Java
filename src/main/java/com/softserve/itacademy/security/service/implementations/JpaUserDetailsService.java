package com.softserve.itacademy.security.service.implementations;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) {

        log.debug("Getting User info via JPA");

        User user = userRepository.findByEmail(email).orElseThrow(() -> {
            return new UsernameNotFoundException("Email address: " + email + " not found");
        });

        return toUserDetails(user);
    }

    private UserDetails toUserDetails(User user) {

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getAuthorities()).build();
    }
}