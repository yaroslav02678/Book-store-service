package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    private EmployeeDTO sampleEmployeeDTO;

    @BeforeEach
    void setUp() {
        sampleEmployeeDTO = new EmployeeDTO();
        sampleEmployeeDTO.setId(1L);
        sampleEmployeeDTO.setName("John Doe");
        sampleEmployeeDTO.setEmail("john.doe@example.com");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateEmployee_whenAdminWithValidData_shouldCallServiceAndRedirect() throws Exception {
        Long employeeId = 1L;
        String validBirthDate = LocalDate.of(1995, 5, 15).format(DateTimeFormatter.ISO_DATE);

        mockMvc.perform(put("/employees/update/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "John Doe Updated")
                        .param("email", "john.doe.updated@example.com")
                        .param("password", "NewSecurePassword123")
                        .param("phone", "+380991234567")
                        .param("birthDate", validBirthDate)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).updateEmployeetById(eq(employeeId), any(EmployeeDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteEmployee_whenAdmin_shouldCallServiceAndRedirect() throws Exception {
        doNothing().when(employeeService).deleteEmployeetById(1L);

        mockMvc.perform(delete("/employees/delete/{id}", 1L)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).deleteEmployeetById(1L);
    }

    @Test
    void getAllEmployees_whenAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/employees"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addEmployee_whenAdminWithValidData_shouldCallServiceAndRedirect() throws Exception {
        String validBirthDate = LocalDate.of(1990, 1, 1).format(DateTimeFormatter.ISO_DATE);

        mockMvc.perform(post("/employees/add")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", "new.employee@example.com")
                        .param("password", "SecurePass123")
                        .param("name", "New Employee Name")
                        .param("phone", "+380501234567")
                        .param("birthDate", validBirthDate)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/employees"));

        verify(employeeService).addEmployee(any(EmployeeDTO.class));
    }
}