package com.softserve.itacademy.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class OwnAuthFilter extends AbstractAuthenticationProcessingFilter {

    public OwnAuthFilter(RequestMatcher requiresAuthenticationRequestMatcher) {
        super(requiresAuthenticationRequestMatcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {

        String email = getEmail(httpServletRequest);
        String password = getPassword(httpServletRequest);

        email = email != null ? email : "";
        email = email.trim();

        password = password != null ? password : "";
        password = password.trim();

        log.debug("Authentication Email: " + email);

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);

        token.setDetails(httpServletRequest);

        return this.getAuthenticationManager().authenticate(token);
    }

    private String getEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    private String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
