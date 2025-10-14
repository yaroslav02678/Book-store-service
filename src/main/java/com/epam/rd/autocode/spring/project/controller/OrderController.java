package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.aop.LoggableSecurityEvent;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final EmployeeService employeeService;

    @Autowired
    public OrderController(OrderService orderService,
                           EmployeeService employeeService) {
        this.orderService = orderService;
        this.employeeService = employeeService;
    }

    @GetMapping("/my/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableSecurityEvent(value = "Адміністратор переглядає всі замовлення", level = Level.INFO)
    public String getAllOrders(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "orderDate") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir,
                               Model model) {

        Sort orderSort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        Pageable orderPageable = PageRequest.of(page, size, orderSort);
        model.addAttribute("orders", orderService.getAllOrders(orderPageable));

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);

        return "orders/admin-orders";
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String deleteOrder(@PathVariable Long id,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "orderDate") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDir) {

        orderService.deleteOrder(id);

        return String.format("redirect:/orders/my/admin?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @PostMapping("/assign")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignEmployee(@RequestParam("orderId") Long orderId,
                                 @RequestParam("employeeId") Long employeeId,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(defaultValue = "orderDate") String sortField,
                                 @RequestParam(defaultValue = "asc") String sortDir) {

        orderService.updateOrderEmployee(orderId, employeeId);

        return String.format("redirect:/orders/my/admin?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @GetMapping("/my/employee")
    @PreAuthorize("hasRole('EMPLOYEE')")
    @LoggableBusinessEvent("Співробітник переглядає свої замовлення")
    public String getEmployeeOrders(Authentication authentication,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "orderDate") String sortField,
                                    @RequestParam(defaultValue = "asc") String sortDir,
                                    Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String userId = (String) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("orders", orderService.getOrdersByEmployee(Long.valueOf(userId), pageable));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);

        return "orders/employee-orders";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam OrderStatus status,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(defaultValue = "orderDate") String sortField,
                               @RequestParam(defaultValue = "asc") String sortDir) {

        orderService.updateOrderStatus(id, status);

        return String.format("redirect:/orders/my/employee?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @GetMapping("/my/client")
    @PreAuthorize("hasRole('CLIENT')")
    @LoggableBusinessEvent("Клієнт переглядає свої замовлення")
    public String getClientOrders(Authentication authentication,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  @RequestParam(defaultValue = "orderDate") String sortField,
                                  @RequestParam(defaultValue = "asc") String sortDir,
                                  Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc")
                ? Sort.by(sortField).ascending()
                : Sort.by(sortField).descending();

        String userId = (String) authentication.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("orders", orderService.getOrdersByClient(Long.valueOf(userId), pageable));
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);

        return "orders/client-orders";
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public String createOrder(Authentication authentication) {
        Long customerId = Long.valueOf((String) authentication.getPrincipal());
        orderService.createOrder(customerId);
        return "redirect:/orders/my/client";
    }

    @PostMapping("/cancel/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public String cancelOrder(@PathVariable Long id,
                              @RequestParam OrderStatus status,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "orderDate") String sortField,
                              @RequestParam(defaultValue = "asc") String sortDir) {

        orderService.updateOrderStatus(id, status);
        return String.format("redirect:/orders/my/client?page=%d&size=%d&sortField=%s&sortDir=%s",
                page, size, sortField, sortDir);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @LoggableSecurityEvent(value = "Адміністратор виконує пошук серед замовлень", level = Level.INFO)
    public String search(@RequestParam("q") String keyword,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "6") int size,
                         @RequestParam(defaultValue = "orderDate") String sortField,
                         @RequestParam(defaultValue = "asc") String sortDir,
                         Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ?
                Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        model.addAttribute("orders", orderService.searchOrders(keyword, pageable));

        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("pageSize", size);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "orders/admin-orders-search";
    }
}



