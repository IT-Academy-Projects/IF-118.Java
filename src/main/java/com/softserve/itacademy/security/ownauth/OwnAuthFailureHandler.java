package com.softserve.itacademy.security.ownauth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softserve.itacademy.exception.dto.BasicExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OwnAuthFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException {

        BasicExceptionResponse dto = BasicExceptionResponse.builder()
                .message(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.value())
                .error(exception.getClass().getSimpleName())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        mapper.writeValue(response.getWriter(), dto);
    }
}
