package com.softserve.itacademy.security.oauth2;


import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.exception.NotFoundException;
import com.softserve.itacademy.repository.UserRepository;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Component
public class OAuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private UserRepository userRepository;

    public OAuthSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (response.isCommitted()) {
            return;
        }

        DefaultOidcUser oidcUser = (DefaultOidcUser) authentication.getPrincipal();
        Map<String, Object> attributes = oidcUser.getAttributes();
        String email = (String) attributes.get("email");

        User user = userRepository.findByEmail(email).orElseThrow(NotFoundException::new);
        Authentication token = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(token);
        if(!user.getIsPickedRole()) {
            getRedirectStrategy().sendRedirect(request, response, "/role-pick");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/user");
        }

    }

}
