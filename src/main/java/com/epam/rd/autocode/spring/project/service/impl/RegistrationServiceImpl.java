package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableSecurityEvent;
import com.epam.rd.autocode.spring.project.dto.ClientRegistrationDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.enums.Role;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    @Autowired
    public RegistrationServiceImpl(PasswordEncoder passwordEncoder, ClientRepository clientRepository) {
        this.passwordEncoder = passwordEncoder;
        this.clientRepository = clientRepository;
    }

    @Override
    @LoggableSecurityEvent("Реєстрація нового клієнта")
    public void registerClient(ClientRegistrationDTO registrationDto) {
        Client newClient = new Client();
        newClient.setName(registrationDto.getName());
        newClient.setEmail(registrationDto.getEmail());
        newClient.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        newClient.setRole(Role.ROLE_CLIENT);

        newClient.setBalance(BigDecimal.valueOf(1000));

        clientRepository.save(newClient);
    }
}
