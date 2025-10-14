package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.repo.*;
import com.epam.rd.autocode.spring.project.service.OrderService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderServiceImpl implements OrderService {
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final CartRepository cartRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            EmployeeRepository employeeRepository,
                            ClientRepository clientRepository,
                            CartRepository cartRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.cartRepository = cartRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }

    @Override
    public Page<OrderDTO> getOrdersByClient(Long id, Pageable pageable) {
        return orderRepository.findByClientId(id, pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }

    @Override
    public Page<OrderDTO> getOrdersByEmployee(Long id, Pageable pageable) {
        return orderRepository.findByEmployeeId(id, pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }

    @Override
    @LoggableBusinessEvent("Створення нового замовлення")
    public OrderDTO createOrder(Long clientId) {
        Client client = clientRepository.findClientById(clientId)
                .orElseThrow(() -> new NotFoundException("error.client.notFound", clientId));

        Order order = new Order();
        order.setClient(client);
        order.setStatus(OrderStatus.NEW);
        order.setOrderDate(LocalDateTime.now());

        Cart cart = cartRepository.findByClientId(clientId)
                .orElseThrow(() -> new NotFoundException("error.cart.notFound", clientId));

        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setPrice(totalPrice);

        for (CartItem cartItem : cart.getItems()) {
            BookItem bookItem = new BookItem();
            bookItem.setBook(cartItem.getBook());
            bookItem.setQuantity(cartItem.getQuantity());
            bookItem.setOrder(order);
            order.getBookItems().add(bookItem);
        }

        Order savedOrder = orderRepository.save(order);
        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        cart.getItems().clear();
        cartRepository.save(cart);

        return orderDTO;
    }

    @Override
    @LoggableBusinessEvent("Призначення співробітника на замовлення")
    public void updateOrderEmployee(Long id, Long employeeId) {
        Order order = orderRepository.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("error.order.notFound", id));
        Employee employee = employeeRepository.findEmployeeById(employeeId)
                .orElseThrow(() -> new NotFoundException("error.employee.notFound", employeeId));
        order.setEmployee(employee);
        orderRepository.save(order);
    }

    @Override
    @LoggableBusinessEvent("Оновлення статусу замовлення")
    public void updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.getOrderById(id)
                        .orElseThrow(() -> new NotFoundException("error.order.notFound", id));

        order.setStatus(status);
        orderRepository.save(order);
    }

    @Transactional
    @LoggableBusinessEvent("Видалення замовлення")
    public void deleteOrder(Long id) {
        Order order = orderRepository.getOrderById(id)
                .orElseThrow(() -> new NotFoundException("error.order.notFound", id));
        orderRepository.delete(order);
        orderRepository.flush();
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.getOrderById(orderId)
                .orElseThrow(() -> new NotFoundException("error.order.notFound", orderId));
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public Page<OrderDTO> searchOrders(String keyword, Pageable pageable) {
        return orderRepository.searchOrders(keyword, pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
    }
}

