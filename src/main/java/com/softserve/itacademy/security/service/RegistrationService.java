package com.softserve.itacademy.security.service;

import com.softserve.itacademy.security.dto.ActivationResponse;
import com.softserve.itacademy.security.dto.RegistrationRequest;
import com.softserve.itacademy.security.dto.SuccessRegistrationResponse;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    @Transactional
    SuccessRegistrationResponse registerUser(RegistrationRequest dto);

    ActivationResponse activateUser(String code);
}
