package com.softserve.itacademy.security.ownauth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.security.auth.login.LoginException;
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
        log.debug("Authentication Email: " + email);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        token.setDetails(httpServletRequest);

        try {
            return this.getAuthenticationManager().authenticate(token);
        } catch (BadCredentialsException e) {
            httpServletRequest.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, e);
            return null;
        }

    }

    private String getEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    private String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
