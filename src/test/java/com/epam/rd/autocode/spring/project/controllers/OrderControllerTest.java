package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.service.EmployeeService;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private EmployeeService employeeService;

    private Authentication adminAuth;
    private Authentication employeeAuth;
    private Authentication clientAuth;

    private final Page<OrderDTO> emptyOrderPage = new PageImpl<>(Collections.emptyList());

    @BeforeEach
    void setUp() {
        adminAuth = new UsernamePasswordAuthenticationToken(
                "1", null, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
        employeeAuth = new UsernamePasswordAuthenticationToken(
                "10", null, AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE"));
        clientAuth = new UsernamePasswordAuthenticationToken(
                "20", null, AuthorityUtils.createAuthorityList("ROLE_CLIENT"));
    }

    @Test
    void getAllOrders_whenAdmin_shouldSucceed() throws Exception {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(emptyOrderPage);
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/orders/my/admin").with(authentication(adminAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/admin-orders"));
    }

    @Test
    void searchOrders_whenAdmin_shouldSucceed() throws Exception {
        when(orderService.searchOrders(anyString(), any(Pageable.class))).thenReturn(emptyOrderPage);
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/orders/search").param("q", "test").with(authentication(adminAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/admin-orders-search"));
    }

    @Test
    void searchOrders_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        when(orderService.searchOrders(anyString(), any(Pageable.class))).thenReturn(emptyOrderPage);
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/orders/search")
                        .param("q", "test")
                        .param("sortDir", "desc")
                        .with(authentication(adminAuth)))
                .andExpect(status().isOk());

        verify(orderService).searchOrders(eq("test"), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getSort().getOrderFor("orderDate").getDirection()).isEqualTo(Sort.Direction.DESC);
    }


    @Test
    void deleteOrder_whenAdmin_shouldSucceed() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(post("/orders/delete/1").with(csrf()).with(authentication(adminAuth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/orders/my/admin?*"));

        verify(orderService).deleteOrder(1L);
    }

    @Test
    void assignEmployee_whenAdmin_shouldSucceed() throws Exception {
        doNothing().when(orderService).updateOrderEmployee(1L, 2L);

        mockMvc.perform(post("/orders/assign").with(csrf())
                        .param("orderId", "1")
                        .param("employeeId", "2")
                        .with(authentication(adminAuth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/orders/my/admin?*"));

        verify(orderService).updateOrderEmployee(1L, 2L);
    }

    @Test
    void getEmployeeOrders_whenEmployee_coversServiceCall() throws Exception {
        Long employeeId = 10L;
        when(orderService.getOrdersByEmployee(eq(employeeId), any(Pageable.class))).thenReturn(emptyOrderPage);

        mockMvc.perform(get("/orders/my/employee").with(authentication(employeeAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/employee-orders"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService).getOrdersByEmployee(eq(employeeId), any(Pageable.class));
    }

    @Test
    void getEmployeeOrders_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        Long employeeId = 10L;
        when(orderService.getOrdersByEmployee(eq(employeeId), any(Pageable.class))).thenReturn(emptyOrderPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/orders/my/employee")
                        .param("sortDir", "desc")
                        .with(authentication(employeeAuth)))
                .andExpect(status().isOk());

        verify(orderService).getOrdersByEmployee(eq(employeeId), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getSort().getOrderFor("orderDate").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void updateStatus_whenEmployee_coversServiceCall() throws Exception {
        Long orderId = 1L;
        OrderStatus status = OrderStatus.COMPLETED;
        doNothing().when(orderService).updateOrderStatus(orderId, status);

        mockMvc.perform(post("/orders/update/{id}", orderId).with(csrf())
                        .param("status", status.name())
                        .with(authentication(employeeAuth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/orders/my/employee?*"));

        verify(orderService).updateOrderStatus(orderId, status);
    }

    @Test
    void getClientOrders_whenClient_coversServiceCall() throws Exception {
        Long clientId = 20L;
        when(orderService.getOrdersByClient(eq(clientId), any(Pageable.class))).thenReturn(emptyOrderPage);

        mockMvc.perform(get("/orders/my/client").with(authentication(clientAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/client-orders"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService).getOrdersByClient(eq(clientId), any(Pageable.class));
    }

    @Test
    void getClientOrders_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        Long clientId = 20L;
        when(orderService.getOrdersByClient(eq(clientId), any(Pageable.class))).thenReturn(emptyOrderPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/orders/my/client").with(authentication(clientAuth))
                        .param("sortDir", "desc")
                        .param("sortField", "orderDate"))
                .andExpect(status().isOk());

        verify(orderService).getOrdersByClient(eq(clientId), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getSort().getOrderFor("orderDate").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void createOrder_whenClient_coversServiceCall() throws Exception {
        Long clientId = 20L;
        when(orderService.createOrder(clientId)).thenReturn(new OrderDTO());

        mockMvc.perform(post("/orders/create").with(csrf()).with(authentication(clientAuth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders/my/client"));

        verify(orderService).createOrder(clientId);
    }

    @Test
    void cancelOrder_whenClient_shouldSucceed() throws Exception {
        Long orderId = 1L;
        OrderStatus status = OrderStatus.CANCELLED;
        doNothing().when(orderService).updateOrderStatus(orderId, status);

        mockMvc.perform(post("/orders/cancel/{id}", orderId).with(csrf())
                        .param("status", status.name())
                        .with(authentication(clientAuth)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/orders/my/client?*"));

        verify(orderService).updateOrderStatus(orderId, status);
    }

    @Test
    void getAllOrders_whenClient_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/orders/my/admin").with(authentication(clientAuth)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getAllOrders_withAscendingSort_shouldUseAscendingSortOrder() throws Exception {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(emptyOrderPage);
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/orders/my/admin")
                        .param("sortDir", "asc")
                        .with(authentication(adminAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/admin-orders"));

        verify(orderService).getAllOrders(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getSort().getOrderFor("orderDate").getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void getAllOrders_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        when(orderService.getAllOrders(any(Pageable.class))).thenReturn(emptyOrderPage);
        when(employeeService.getAllEmployees()).thenReturn(Collections.emptyList());
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/orders/my/admin")
                        .param("sortDir", "desc")
                        .with(authentication(adminAuth)))
                .andExpect(status().isOk())
                .andExpect(view().name("orders/admin-orders"));

        verify(orderService).getAllOrders(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getSort().getOrderFor("orderDate").getDirection())
                .isEqualTo(Sort.Direction.DESC);
    }
}