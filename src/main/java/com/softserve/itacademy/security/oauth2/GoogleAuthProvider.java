//package com.softserve.itacademy.security.oauth2;
//
//import com.softserve.itacademy.entity.User;
//import com.softserve.itacademy.exception.NotFoundException;
//import com.softserve.itacademy.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationProvider;
//import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
//import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
//
//@Slf4j
//@Component
//public class GoogleAuthProvider extends AuthenticationProvider {
//
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    OAuth2LoginAuthenticationProvider oAuth2LoginAuthenticationProvider;
//
//    public GoogleAuthProvider() {
//
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        Authentication auth = super.authenticate(authentication);
//
//
//        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
//        Map<String, Object> attributes = oidcUser.getAttributes();
//        String email = (String) attributes.get("email");
//
//        User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
//        Authentication token = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword(), user.getAuthorities());
//
//        return token;
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        return OAuth2LoginAuthenticationToken.class.isAssignableFrom(aClass);
//    }
//}
