package com.softserve.itacademy.controller;

import com.softserve.itacademy.exception.dto.BasicExceptionResponse;
import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.RegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    private final RegistrationService registrationService;

    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<SuccessRegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest dto) {
        return new ResponseEntity<>(registrationService.registerUser(dto), HttpStatus.CREATED);
    }

    @GetMapping("/activation/{code}")
    public ResponseEntity<ActivationResponse> activation(@PathVariable String code) {
        return new ResponseEntity<>(registrationService.activateUser(code), HttpStatus.OK);
    }

    @GetMapping("/login-error")
    public ResponseEntity<BasicExceptionResponse> loginError(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        BasicExceptionResponse dto = null;

        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                dto = BasicExceptionResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error(ex.getClass().getSimpleName())
                        .build();
            }
        }

        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }
}
