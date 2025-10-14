package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository,
                             ModelMapper modelMapper,
                             PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<ClientDTO> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable)
                .map(client -> modelMapper.map(client, ClientDTO.class));
    }

    @Override
    public ClientDTO getClientById(long id) {
        return clientRepository.findClientById(id)
                .map(client -> modelMapper.map(client, ClientDTO.class))
                .orElseThrow(() -> new NotFoundException("error.client.notFound", id));
    }

    @Override
    @LoggableBusinessEvent("Оновлення даних клієнта за ID")
    public ClientDTO updateClientById(long id, ClientDTO clientDTO) {
        Client client = clientRepository.findClientById(id)
                .map(clientDTO1 -> modelMapper.map(clientDTO, Client.class))
                .orElseThrow(() -> new NotFoundException("error.client.notFound", id));

        client.setEmail(clientDTO.getEmail());
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        client.setName(clientDTO.getName());
        client.setBalance(clientDTO.getBalance());

        clientRepository.save(client);
        return modelMapper.map(client, ClientDTO.class);
    }

    @Override
    @LoggableBusinessEvent("Видалення клієнта за ID")
    public void deleteClientById(long id) {
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new NotFoundException("error.client.notFound", id);
        }
    }

    @Override
    @LoggableBusinessEvent("Додавання нового клієнта")
    public ClientDTO addClient(ClientDTO clientDTO) {
        if(clientRepository.existsByNameIgnoreCase(clientDTO.getName())) {
            throw new AlreadyExistException("error.client.alreadyExists", clientDTO.getName());
        }

        Client client = modelMapper.map(clientDTO, Client.class);
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        Client savedClient = clientRepository.save(client);
        return modelMapper.map(savedClient, ClientDTO.class);
    }
}

