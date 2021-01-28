package com.softserve.itacademy.service;

import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.RolePickRequest;
import com.softserve.itacademy.security.dto.RolePickResponse;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {

    @Transactional
    SuccessRegistrationResponse registerUser(RegistrationRequest dto);

    ActivationResponse activateUser(String code);

    RolePickResponse pickRole(Integer userId, RolePickRequest request);
}
