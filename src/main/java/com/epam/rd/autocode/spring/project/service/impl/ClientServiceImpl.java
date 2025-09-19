package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientByEmail(String email) {
        Client client = clientRepository.findClientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return mapToDTO(client);
    }

    @Override
    public ClientDTO updateClientByEmail(String email, ClientDTO clientDTO) {
        Client client = clientRepository.findClientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        client.setPassword(clientDTO.getPassword());
        client.setName(clientDTO.getName());
        client.setBalance(clientDTO.getBalance());

        Client updatedClient = clientRepository.save(client);
        return mapToDTO(updatedClient);
    }

    @Override
    public void deleteClientByEmail(String email) {
        Client client = clientRepository.findClientByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        clientRepository.delete(client);
    }

    @Override
    public ClientDTO addClient(ClientDTO clientDTO) {
        Client client = new Client();
        client.setEmail(clientDTO.getEmail());
        client.setPassword(clientDTO.getPassword());
        client.setName(clientDTO.getName());
        client.setBalance(clientDTO.getBalance());

        Client savedClient = clientRepository.save(client);
        return mapToDTO(savedClient);
    }

    private ClientDTO mapToDTO(Client client) {
        return new ClientDTO(
                client.getEmail(),
                client.getPassword(),
                client.getName(),
                client.getBalance()
        );
    }
}

