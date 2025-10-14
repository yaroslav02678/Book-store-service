package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.ClientRegistrationDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.enums.Role;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.impl.RegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationServiceImpl clientService;

    @Captor
    private ArgumentCaptor<Client> clientArgumentCaptor;

    @Test
    void testRegisterClient_Success() {
        ClientRegistrationDTO registrationDto = new ClientRegistrationDTO();
        registrationDto.setName("Test User");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");

        String encodedPassword = "encodedPassword123";
        when(passwordEncoder.encode(registrationDto.getPassword())).thenReturn(encodedPassword);

        clientService.registerClient(registrationDto);

        verify(clientRepository).save(clientArgumentCaptor.capture());
        Client savedClient = clientArgumentCaptor.getValue();

        assertNotNull(savedClient);
        assertEquals(registrationDto.getName(), savedClient.getName());
        assertEquals(registrationDto.getEmail(), savedClient.getEmail());
        assertEquals(encodedPassword, savedClient.getPassword());
        assertEquals(Role.ROLE_CLIENT, savedClient.getRole());
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(savedClient.getBalance()));
    }
}