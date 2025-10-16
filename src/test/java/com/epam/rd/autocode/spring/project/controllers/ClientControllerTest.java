package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;


    @Test
    @WithMockUser(roles = "ADMIN")
    void addClient_whenAdmin_shouldSucceedAndRedirect() throws Exception {
        when(clientService.addClient(any(ClientDTO.class))).thenReturn(new ClientDTO());
        mockMvc.perform(post("/clients/add").with(csrf()).flashAttr("client", new ClientDTO()))
                .andExpect(status().isFound());
        verify(clientService).addClient(any(ClientDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateClient_whenAdmin_shouldSucceedAndRedirect() throws Exception {
        when(clientService.updateClientById(anyLong(), any(ClientDTO.class))).thenReturn(new ClientDTO());
        mockMvc.perform(put("/clients/update/1").with(csrf()).flashAttr("client", new ClientDTO()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));
        verify(clientService).updateClientById(anyLong(), any(ClientDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteClient_whenAdmin_shouldSucceedAndRedirect() throws Exception {
        doNothing().when(clientService).deleteClientById(1L);
        mockMvc.perform(delete("/clients/delete/1").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/clients"));
        verify(clientService).deleteClientById(1L);
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void getClient_whenClient_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isUnauthorized());
    }
}