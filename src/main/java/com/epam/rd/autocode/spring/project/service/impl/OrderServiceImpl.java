package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            EmployeeRepository employeeRepository,
                            ClientRepository clientRepository,
                            BookRepository bookRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<OrderDTO> getOrdersByClient(String clientEmail) {
        return orderRepository.findAllByClient_Email(clientEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        return orderRepository.findAllByEmployee_Email(employeeEmail).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();

        Client client = clientRepository.findClientByEmail(orderDTO.getClientEmail())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Employee employee = employeeRepository.findEmployeeByEmail(orderDTO.getEmployeeEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        newOrder.setClient(client);
        newOrder.setEmployee(employee);
        newOrder.setOrderDate(orderDTO.getOrderDate());
        newOrder.setPrice(orderDTO.getPrice());

        List<BookItem> bookItems = orderDTO.getBookItems().stream()
                .map(biDTO -> {
                    Book book = bookRepository.findBookByName(biDTO.getBookName())
                            .orElseThrow(() -> new RuntimeException("Book not found"));
                    BookItem bookItem = new BookItem();
                    bookItem.setBook(book);
                    bookItem.setQuantity(biDTO.getQuantity());
                    bookItem.setOrder(newOrder);
                    return bookItem;
                }).collect(Collectors.toList());

        newOrder.setBookItems(bookItems);

        Order savedOrder = orderRepository.save(newOrder);
        return mapToDTO(savedOrder);
    }

    private OrderDTO mapToDTO(Order order) {
        return new OrderDTO(
                order.getClient().getEmail(),
                order.getEmployee().getEmail(),
                order.getOrderDate(),
                order.getPrice(),
                order.getBookItems().stream()
                        .map(bi -> new BookItemDTO(bi.getBook().getName(), bi.getQuantity()))
                        .collect(Collectors.toList())
        );
    }
}

