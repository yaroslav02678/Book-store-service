package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import com.epam.rd.autocode.spring.project.repo.*;
import com.epam.rd.autocode.spring.project.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Client client;
    private Employee employee;
    private Order order;
    private Cart cart;
    private Book book;

    @Test
    void createOrder_whenClientAndCartExist_createsOrder() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.of(cart));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(new OrderDTO());

        OrderDTO result = orderService.createOrder(1L);

        assertNotNull(result);
        verify(orderRepository).save(argThat(o ->
                o.getPrice().compareTo(new BigDecimal("40.00")) == 0 &&
                        !o.getBookItems().isEmpty()
        ));
        verify(cartRepository).save(cart);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void createOrder_whenClientNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.createOrder(99L));
    }

    @Test
    void createOrder_whenCartNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.createOrder(1L));
    }

    @Test
    void updateOrderEmployee_whenOrderAndEmployeeExist_assignsEmployee() {
        when(orderRepository.getOrderById(100L)).thenReturn(Optional.of(order));
        when(employeeRepository.findEmployeeById(10L)).thenReturn(Optional.of(employee));

        orderService.updateOrderEmployee(100L, 10L);

        verify(orderRepository).save(order);
        assertEquals(employee, order.getEmployee());
    }

    @Test
    void updateOrderStatus_whenStatusIsNew_updatesStatus() {
        when(orderRepository.getOrderById(100L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(100L, OrderStatus.IN_PROGRESS);

        verify(orderRepository).save(order);
        assertEquals(OrderStatus.IN_PROGRESS, order.getStatus());
    }

    @Test
    void updateOrderStatus_shouldUpdateStatusAndSave() {
        order.setStatus(OrderStatus.COMPLETED);
        when(orderRepository.getOrderById(100L)).thenReturn(Optional.of(order));

        orderService.updateOrderStatus(100L, OrderStatus.CANCELLED);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(orderRepository).save(order);
    }


    @Test
    void deleteOrder_whenOrderExists_deletesOrder() {
        when(orderRepository.getOrderById(100L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(100L);

        verify(orderRepository).delete(order);
        verify(orderRepository).flush();
    }

    @Test
    void searchOrders_whenOrdersFound_returnsMappedPageOfDTOs() {
        Pageable pageable = PageRequest.of(0, 5);
        String keyword = "test";

        Order order = new Order();
        order.setId(1L);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);

        Page<Order> orderPage = new PageImpl<>(List.of(order));

        when(orderRepository.searchOrders(keyword, pageable)).thenReturn(orderPage);
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        Page<OrderDTO> result = orderService.searchOrders(keyword, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());

        verify(orderRepository).searchOrders(keyword, pageable);
        verify(modelMapper).map(order, OrderDTO.class);
    }

    @Test
    void getOrderById_whenOrderExists_returnsCorrectDTO() {
        Order order = new Order();
        order.setId(1L);
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(1L);

        when(orderRepository.getOrderById(1L)).thenReturn(Optional.of(order));
        when(modelMapper.map(order, OrderDTO.class)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(orderRepository).getOrderById(1L);
        verify(modelMapper).map(order, OrderDTO.class);
    }

    @Test
    void getOrderById_whenOrderDoesNotExist_throwsNotFoundException() {
        when(orderRepository.getOrderById(999L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> orderService.getOrderById(999L));
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getAllOrders_whenOrdersExist_returnsPageOfDTOs() {
        Page<Order> orderPage = new PageImpl<>(List.of(new Order()));
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(orderPage);
        when(modelMapper.map(any(Order.class), eq(OrderDTO.class))).thenReturn(new OrderDTO());

        Page<OrderDTO> result = orderService.getAllOrders(PageRequest.of(0, 5));

        assertFalse(result.isEmpty());
        verify(modelMapper).map(any(Order.class), eq(OrderDTO.class));
    }

    @Test
    void getAllOrders_whenNoOrdersExist_returnsEmptyPage() {
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());

        Page<OrderDTO> result = orderService.getAllOrders(PageRequest.of(0, 5));
        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getOrdersByClient_whenClientHasOrders_returnsPageOfDTOs() {
        Page<Order> orderPage = new PageImpl<>(List.of(new Order()));
        when(orderRepository.findByClientId(anyLong(), any(Pageable.class))).thenReturn(orderPage);
        Page<OrderDTO> result = orderService.getOrdersByClient(1L, PageRequest.of(0, 5));
        assertFalse(result.isEmpty());
    }

    @Test
    void getOrdersByClient_whenClientHasNoOrders_returnsEmptyPage() {
        when(orderRepository.findByClientId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());
        Page<OrderDTO> result = orderService.getOrdersByClient(1L, PageRequest.of(0, 5));
        assertTrue(result.isEmpty());
    }

    @Test
    void getOrdersByEmployee_whenEmployeeHasOrders_returnsPageOfDTOs() {
        Page<Order> orderPage = new PageImpl<>(List.of(new Order()));
        when(orderRepository.findByEmployeeId(anyLong(), any(Pageable.class))).thenReturn(orderPage);
        Page<OrderDTO> result = orderService.getOrdersByEmployee(1L, PageRequest.of(0, 5));
        assertFalse(result.isEmpty());
    }

    @Test
    void getOrdersByEmployee_whenEmployeeHasNoOrders_returnsEmptyPage() {
        when(orderRepository.findByEmployeeId(anyLong(), any(Pageable.class))).thenReturn(Page.empty());
        Page<OrderDTO> result = orderService.getOrdersByEmployee(1L, PageRequest.of(0, 5));
        assertTrue(result.isEmpty());
    }

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);

        employee = new Employee();
        employee.setId(10L);

        book = new Book();
        book.setPrice(new BigDecimal("20.00"));

        CartItem cartItem = new CartItem();
        cartItem.setBook(book);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setClientId(1L);
        cart.setItems(new ArrayList<>(List.of(cartItem)));

        order = new Order();
        order.setId(100L);
        order.setStatus(OrderStatus.NEW);
        order.setClient(client);
    }
}