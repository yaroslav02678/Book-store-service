package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderDTO> getAllOrders(Pageable pageable);

    Page<OrderDTO> getOrdersByClient(Long id, Pageable pageable);

    Page<OrderDTO> getOrdersByEmployee(Long id, Pageable pageable);

    OrderDTO createOrder(Long clientId);

    void updateOrderEmployee(Long id, Long employeeId);

    void updateOrderStatus(Long id, OrderStatus status);

    void deleteOrder(Long id);

    OrderDTO getOrderById(Long orderId);

    Page<OrderDTO> searchOrders(String keyword, Pageable pageable);
}
