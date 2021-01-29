package com.softserve.itacademy.controller;

import com.softserve.itacademy.entity.User;
import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.PasswordByTokenRequest;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.ResetPasswordRequest;
import com.softserve.itacademy.security.dto.RolePickRequest;
import com.softserve.itacademy.security.dto.RolePickResponse;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import com.softserve.itacademy.service.AuthService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

import static com.softserve.itacademy.config.Constance.API_V1;

@RestController
@RequestMapping(API_V1)
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registration")
    public ResponseEntity<SuccessRegistrationResponse> register(
            @Valid @RequestBody RegistrationRequest dto) {
        return new ResponseEntity<>(authService.registerUser(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/role-pick")
    public ResponseEntity<RolePickResponse> rolePick(
            @Valid @RequestBody RolePickRequest dto,
            @AuthenticationPrincipal User user) {
        return new ResponseEntity<>(authService.pickRole(user.getId(), dto), HttpStatus.OK);
    }

    @GetMapping("/activation/{code}")
    public ResponseEntity<ActivationResponse> activation(@PathVariable String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/login"));

        return new ResponseEntity<>(authService.activateUser(code), headers, HttpStatus.MOVED_PERMANENTLY);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {

        authService.resetPassword(resetPasswordRequest);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @PostMapping("/password-reset/new")
    public ResponseEntity<Void> setPasswordByToken(@Valid PasswordByTokenRequest dto) {

        authService.setPasswordByToken(dto);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
}