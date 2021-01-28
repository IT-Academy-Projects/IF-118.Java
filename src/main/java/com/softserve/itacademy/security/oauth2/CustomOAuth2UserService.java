package com.softserve.itacademy.security.oauth2;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.OAuth2AuthenticationProcessingException;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        if (!attributes.containsKey("email") || attributes.get("email").toString().isEmpty()) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        String email = attributes.get("email").toString();
        String name = attributes.get("name").toString();

        processUser(email, name);
        return oAuth2User;
    }

    private void processUser(String email, String name) {
        User user = userRepository.findByEmail(email).orElse(
                User.builder()
                        .email(email)
                        .isPickedRole(false)
                        .activated(true)
                        .build());

        user.setName(name);
        userRepository.save(user);
    }
}
