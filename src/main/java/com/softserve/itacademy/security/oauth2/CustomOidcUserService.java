package com.softserve.itacademy.security.oauth2;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.repository.UserRepository;
import com.softserve.itacademy.repository.security.RoleRepository;
import com.softserve.itacademy.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    private final RoleService roleService;

    @Transactional
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        OidcUser oidcUser = super.loadUser(userRequest);

        Map<String, Object> attributes = oidcUser.getAttributes();

        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        updateUser(email, name);

        return oidcUser;
    }

    private void updateUser(String email, String name) {
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setName(name);
        user.setActivated(true);

        if(user.getAuthorities().isEmpty()) {
            user.addRole(roleService.findByName("USER"));
            user.addRole(roleService.findByName("STUDENT")); //TODO: Add ability to pick a role with google sign up
        }


        userRepository.save(user);
    }

}
