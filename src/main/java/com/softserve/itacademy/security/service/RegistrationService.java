package com.softserve.itacademy.security.service;

import com.softserve.itacademy.security.dto.RegistrationDto;
import org.springframework.transaction.annotation.Transactional;

public interface RegistrationService {
    @Transactional
    String registerUser(RegistrationDto dto);
}
