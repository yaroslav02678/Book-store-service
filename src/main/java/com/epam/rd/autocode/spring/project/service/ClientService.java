package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ClientService {

    Page<ClientDTO> getAllClients(Pageable pageable);

    ClientDTO getClientById(long id);

    ClientDTO updateClientById(long id, ClientDTO client);

    void deleteClientById(long id);

    ClientDTO addClient(ClientDTO client);
}
