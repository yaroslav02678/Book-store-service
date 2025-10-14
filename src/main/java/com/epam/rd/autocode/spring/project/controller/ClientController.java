package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clients")
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllClients(@RequestParam(defaultValue = "0") int pages,
                                @RequestParam(defaultValue = "10") int size,
                                Model model) {
        model.addAttribute("clients", clientService.getAllClients(PageRequest.of(pages, size)));
        return "clients/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getClient(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.getClientById(id));
        return "clients/detail-by-email";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String addClient(@ModelAttribute("client") ClientDTO clientDTO) {
        clientService.addClient(clientDTO);
        return "redirect:/clients";
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String updateClient(@PathVariable Long id, @ModelAttribute("client") ClientDTO clientDTO) {
        clientService.updateClientById(id, clientDTO);
        return "redirect:/clients";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteClient(@PathVariable Long id) {
        clientService.deleteClientById(id);
        return "redirect:/clients";
    }
}
