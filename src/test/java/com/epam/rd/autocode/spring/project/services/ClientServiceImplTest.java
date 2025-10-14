package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    private Client client;
    private ClientDTO clientDTO;

    @Test
    void getClientById_whenClientExists_returnsClientDTO() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        ClientDTO result = clientService.getClientById(1L);

        assertNotNull(result);
        assertEquals(client.getName(), result.getName());
        verify(clientRepository).findClientById(1L);
    }

    @Test
    void getClientById_whenClientNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.getClientById(99L));
    }

    @Test
    void addClient_whenClientIsNew_returnsSavedClientDTO() {
        when(clientRepository.existsByNameIgnoreCase("Test Client")).thenReturn(false);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        ClientDTO result = clientService.addClient(clientDTO);

        assertNotNull(result);
        assertEquals("encodedPassword", client.getPassword());
        verify(clientRepository).save(client);
    }

    @Test
    void addClient_whenClientExists_throwsAlreadyExistException() {
        when(clientRepository.existsByNameIgnoreCase("Test Client")).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> clientService.addClient(clientDTO));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void updateClientById_whenClientExists_updatesAndReturnsClientDTO() {
        Client existingClient = new Client(); // Simulate the client found in DB
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(existingClient));

        when(modelMapper.map(clientDTO, Client.class)).thenReturn(client);
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        ClientDTO result = clientService.updateClientById(1L, clientDTO);

        assertNotNull(result);
        verify(clientRepository).save(any(Client.class));
        verify(passwordEncoder).encode("rawPassword");
    }


    @Test
    void updateClientById_whenClientNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> clientService.updateClientById(99L, clientDTO));
        verify(clientRepository, never()).save(any());
    }

    @Test
    void deleteClientById_whenClientExists_deletesClient() {
        doNothing().when(clientRepository).deleteById(1L);

        clientService.deleteClientById(1L);

        verify(clientRepository).deleteById(1L);
    }

    @Test
    void deleteClientById_whenClientNotFound_throwsNotFoundException() {
        doThrow(new EmptyResultDataAccessException(1)).when(clientRepository).deleteById(99L);

        assertThrows(NotFoundException.class, () -> clientService.deleteClientById(99L));
        verify(clientRepository).deleteById(99L);
    }

    @Test
    void getAllClients_whenClientsExist_returnsPageOfDTOs() {
        Pageable pageable = PageRequest.of(0, 5);

        Client client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Test Client");

        Page<Client> clientPage = new PageImpl<>(List.of(client));

        when(clientRepository.findAll(pageable)).thenReturn(clientPage);
        when(modelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        Page<ClientDTO> result = clientService.getAllClients(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());

        verify(clientRepository).findAll(pageable);
        verify(modelMapper).map(client, ClientDTO.class);
    }

    @Test
    void getAllClients_whenNoClientsExist_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);

        when(clientRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<ClientDTO> result = clientService.getAllClients(pageable);

        assertTrue(result.isEmpty());

        verify(clientRepository).findAll(pageable);
        verify(modelMapper, never()).map(any(), any());
    }

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setName("Test Client");
        client.setEmail("test@client.com");
        client.setPassword("encodedPassword");

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Test Client");
        clientDTO.setEmail("test@client.com");
        clientDTO.setPassword("rawPassword");
    }
}