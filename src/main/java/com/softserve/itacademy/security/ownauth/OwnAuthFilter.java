package com.softserve.itacademy.security.ownauth;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Getter
@Setter
public class OwnAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final OwnAuthFailureHandler ownAuthFailureHandler = new OwnAuthFailureHandler();

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

        return this.getAuthenticationManager().authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        log.debug("Set SecurityContextHolder to {}", authResult);
        super.getRememberMeServices().loginSuccess(request, response, authResult);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        SecurityContextHolder.clearContext();
        super.getRememberMeServices().loginFail(request, response);
        this.ownAuthFailureHandler.onAuthenticationFailure(request, response, exception);
    }


    private String getEmail(HttpServletRequest request) {
        return request.getParameter("email");
    }

    private String getPassword(HttpServletRequest request) {
        return request.getParameter("password");
    }
}
