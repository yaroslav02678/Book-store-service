package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.dto.EmployeeDTO;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getAllEmployees(@RequestParam(defaultValue = "0") int pages,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        model.addAttribute("employees", employeeService.getAllEmployees(PageRequest.of(pages, size)));
        return "employees/list";
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String getEmployeeByEmail(@PathVariable Long id, Model model) {
        EmployeeDTO employee = employeeService.getEmployeetById(id);
        model.addAttribute("employee", employee);
        return "employees/detail-by-email";
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String addEmployee(@Valid @ModelAttribute("employee") EmployeeDTO employeeDTO) {
        employeeService.addEmployee(employeeDTO);
        return "redirect:/employees";
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String updateEmployee(@PathVariable Long id,
                                 @Valid @ModelAttribute("employee") EmployeeDTO employeeDTO) {
        employeeService.updateEmployeetById(id, employeeDTO);
        return "redirect:/employees";
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeetById(id);
        return "redirect:/employees";
    }
}
