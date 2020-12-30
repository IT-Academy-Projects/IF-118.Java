package com.softserve.itacademy.controller;

import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.security.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    RegistrationService registrationService;

    @Autowired
    public AuthController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/registration")
    public ResponseEntity<SuccessRegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest dto
    ) {
        return new ResponseEntity<>(registrationService.registerUser(dto), HttpStatus.CREATED);
    }

    @GetMapping("/login-error")
    public ResponseEntity<String> loginError(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        String errorMessage = null;

        if (session != null) {
            AuthenticationException ex = (AuthenticationException) session
                    .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
            if (ex != null) {
                errorMessage = ex.getMessage();
            }
        }

        return new ResponseEntity<String>(errorMessage, HttpStatus.BAD_REQUEST);
    }

}
