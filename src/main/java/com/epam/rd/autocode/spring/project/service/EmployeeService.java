package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.awt.*;
import java.util.List;

public interface EmployeeService {

    Page<EmployeeDTO> getAllEmployees(Pageable pageable);

    List<Employee> getAllEmployees();

    EmployeeDTO getEmployeetById(long id);

    EmployeeDTO updateEmployeetById(long id, EmployeeDTO employeeDTO);

    void deleteEmployeetById(long id);

    EmployeeDTO addEmployee(EmployeeDTO employeeDTO);
}
