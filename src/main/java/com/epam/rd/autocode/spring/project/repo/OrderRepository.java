package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findAllByClient_Email(String clientEmail);

    List<Order> findAllByEmployee_Email(String employeeEmail);
}
