package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @Test
    void getAllEmployees_withPageable_whenEmployeesExist_returnsPageOfDTOs() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Employee> employeePage = new PageImpl<>(List.of(employee));
        when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        Page<EmployeeDTO> result = employeeServiceImpl.getAllEmployees(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(modelMapper).map(employee, EmployeeDTO.class);
    }

    @Test
    void getAllEmployees_withPageable_whenNoEmployees_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);
        when(employeeRepository.findAll(pageable)).thenReturn(Page.empty());

        Page<EmployeeDTO> result = employeeServiceImpl.getAllEmployees(pageable);

        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getAllEmployees_asList_returnsListOfEmployees() {
        when(employeeRepository.findAll()).thenReturn(List.of(employee));

        List<Employee> result = employeeServiceImpl.getAllEmployees();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getEmployeeById_whenEmployeeExists_returnsDTO() {
        when(employeeRepository.findEmployeeById(1L)).thenReturn(Optional.of(employee));
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        EmployeeDTO result = employeeServiceImpl.getEmployeetById(1L);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getEmployeeById_whenEmployeeNotFound_throwsNotFoundException() {
        when(employeeRepository.findEmployeeById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeServiceImpl.getEmployeetById(99L));
    }

    @Test
    void addEmployee_whenNameAlreadyExists_throwsAlreadyExistException() {
        when(employeeRepository.existsByNameIgnoreCase("John Doe")).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> employeeServiceImpl.addEmployee(employeeDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void addEmployee_whenNewEmployee_encodesPasswordAndSaves() {
        when(employeeRepository.existsByNameIgnoreCase(anyString())).thenReturn(false);
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(employee);
        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
        when(modelMapper.map(employee, EmployeeDTO.class)).thenReturn(employeeDTO);

        employeeServiceImpl.addEmployee(employeeDTO);

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();
        assertEquals("encodedPassword", savedEmployee.getPassword());
    }

    @Test
    void updateEmployeeById_whenEmployeeNotFound_throwsNotFoundException() {
        when(employeeRepository.findEmployeeById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> employeeServiceImpl.updateEmployeetById(99L, employeeDTO));
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void updateEmployeeById_whenEmployeeExists_updatesAllFieldsAndSaves() {
        Employee existingEmployee = new Employee();
        existingEmployee.setId(1L);

        // Цей маппінг відбувається всередині .map() Optional
        when(employeeRepository.findEmployeeById(1L)).thenReturn(Optional.of(existingEmployee));
        when(modelMapper.map(employeeDTO, Employee.class)).thenReturn(existingEmployee);

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(employeeRepository.save(any(Employee.class))).thenReturn(existingEmployee);
        when(modelMapper.map(existingEmployee, EmployeeDTO.class)).thenReturn(employeeDTO);

        // Оновлюємо DTO новими даними
        employeeDTO.setEmail("new.email@example.com");
        employeeDTO.setName("Jane Doe");
        employeeDTO.setPhone("123-456-7890");
        employeeDTO.setBirthDate(LocalDate.of(2000, 1, 1));

        employeeServiceImpl.updateEmployeetById(1L, employeeDTO);

        ArgumentCaptor<Employee> employeeCaptor = ArgumentCaptor.forClass(Employee.class);
        verify(employeeRepository).save(employeeCaptor.capture());

        Employee savedEmployee = employeeCaptor.getValue();

        assertEquals("new.email@example.com", savedEmployee.getEmail());
        assertEquals("encodedPassword", savedEmployee.getPassword());
        assertEquals("Jane Doe", savedEmployee.getName());
        assertEquals("123-456-7890", savedEmployee.getPhone());
        assertEquals(LocalDate.of(2000, 1, 1), savedEmployee.getBirthDate());
    }

    @Test
    void deleteEmployeeById_callsRepositoryDelete() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeServiceImpl.deleteEmployeetById(1L);

        verify(employeeRepository).deleteById(1L);
    }

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setPassword("encodedPassword");

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("John Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setPassword("rawPassword");
    }
}