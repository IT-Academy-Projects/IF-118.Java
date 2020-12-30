package com.softserve.itacademy.security.service.implementation;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.RegistrationService;
import com.softserve.itacademy.service.RoleService;
import com.softserve.itacademy.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RegistrationServiceImpl implements RegistrationService {

    private RoleService roleService;

    private UserService userService;

    private PasswordEncoder passwordEncoder;

//    @Autowired
//    public DefaultRegistrationService(RoleService roleService, UserService userService) {
//        this.roleService = roleService;
//        this.userService = userService;
//    }


    public RegistrationServiceImpl(RoleService roleService, UserService userService, PasswordEncoder passwordEncoder) {
        this.roleService = roleService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

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
                .role(roleService.findByName(dto.getPickedRole())).build();

        userService.addUser(user);

         return SuccessRegistrationResponse.builder().email(user.getEmail()).name(user.getName()).id(user.getId()).build();
    }
}
