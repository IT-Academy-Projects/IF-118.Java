package com.softserve.itacademy.security.service;

import com.softserve.itacademy.security.dto.RegistrationDto;
import com.softserve.itacademy.security.dto.SuccessRegistrationDto;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    @Transactional
    SuccessRegistrationDto registerUser(RegistrationDto dto);
}
