package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               ModelMapper modelMapper,
                               PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<EmployeeDTO> getAllEmployees(Pageable pageable) {
        return employeeRepository.findAll(pageable)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public EmployeeDTO getEmployeetById(long id) {
        return employeeRepository.findEmployeeById(id)
                .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                .orElseThrow(() -> new NotFoundException("Employee.not.found", id));
    }

    @Override
    @LoggableBusinessEvent("Оновлення даних співробітника за ID")
    public EmployeeDTO updateEmployeetById(long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findEmployeeById(id)
                .map(employee1 -> modelMapper.map(employeeDTO, Employee.class))
                .orElseThrow(() -> new NotFoundException("Employee.not.found", id));

        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        employee.setName(employeeDTO.getName());
        employee.setPhone(employeeDTO.getPhone());
        employee.setBirthDate(employeeDTO.getBirthDate());

        employeeRepository.save(employee);
        return modelMapper.map(employee, EmployeeDTO.class);
    }

    @Override
    @LoggableBusinessEvent("Видалення співробітника за ID")
    public void deleteEmployeetById(long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    @LoggableBusinessEvent("Додавання нового співробітника")
    public EmployeeDTO addEmployee(EmployeeDTO employeeDTO) {
        if(employeeRepository.existsByNameIgnoreCase(employeeDTO.getName())) {
            throw new AlreadyExistException("error.employee.alreadyExists", employeeDTO.getName());
        }

        Employee employee = modelMapper.map(employeeDTO, Employee.class);
        employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }
}

