package com.softserve.itacademy.security.service.implementations;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.security.dto.RegistrationDto;
import com.softserve.itacademy.security.dto.SuccessRegistrationDto;
import com.softserve.itacademy.security.service.RegistrationService;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class DefaultRegistrationService implements RegistrationService {

    private RoleService roleService;

    private UserService userService;

    @Autowired
    public DefaultRegistrationService(RoleService roleService, UserService userService) {
        this.roleService = roleService;
        this.userService = userService;
    }

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    @Transactional
    @Override
    public SuccessRegistrationDto registerUser(RegistrationDto dto) {

        if (!(dto.getPickedRole().equals("STUDENT") || dto.getPickedRole().equals("TEACHER"))) {
            throw new BadCredentialsException("You can't pick such role");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(roleService.findByName("USER"))
                .role(roleService.findByName(dto.getPickedRole())).build();

        userService.addUser(user);

         return SuccessRegistrationDto.builder().email(user.getEmail()).name(user.getName()).id(user.getId()).build();
    }
}
