package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookItemDTO;
import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.*;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.repo.EmployeeRepository;
import com.epam.rd.autocode.spring.project.repo.OrderRepository;
import com.epam.rd.autocode.spring.project.service.OrderService;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            EmployeeRepository employeeRepository,
                            ClientRepository clientRepository,
                            BookRepository bookRepository,
                            ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.employeeRepository = employeeRepository;
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<OrderDTO> getOrdersByClient(String clientEmail) {
        return orderRepository.findByClient_Email(clientEmail).stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByEmployee(String employeeEmail) {
        return orderRepository.findByEmployee_Email(employeeEmail).stream()
                .map(order ->modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }


    @Override
    public OrderDTO addOrder(OrderDTO orderDTO) {
        Order newOrder = new Order();

        Client client = clientRepository.findByEmail(orderDTO.getClientEmail())
                .orElseThrow(() -> new RuntimeException("Client not found"));
        Employee employee = employeeRepository.findByEmail(orderDTO.getEmployeeEmail())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        newOrder.setClient(client);
        newOrder.setEmployee(employee);
        newOrder.setOrderDate(orderDTO.getOrderDate());
        newOrder.setPrice(orderDTO.getPrice());

        List<BookItem> bookItems = orderDTO.getBookItems().stream()
                .map(biDTO -> {
                    Book book = bookRepository.findByName(biDTO.getBookName())
                            .orElseThrow(() -> new RuntimeException("Book not found"));
                    BookItem bookItem = new BookItem();
                    bookItem.setBook(book);
                    bookItem.setQuantity(biDTO.getQuantity());
                    bookItem.setOrder(newOrder);
                    return bookItem;
                }).collect(Collectors.toList());

        newOrder.setBookItems(bookItems);

        Order savedOrder = orderRepository.save(newOrder);
        return modelMapper.map(savedOrder, OrderDTO.class);
    }
}

