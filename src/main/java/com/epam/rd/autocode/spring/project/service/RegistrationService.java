package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientRegistrationDTO;

public interface RegistrationService {
    void registerClient(ClientRegistrationDTO registrationDto);
}
