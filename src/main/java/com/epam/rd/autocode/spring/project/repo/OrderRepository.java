package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.dto.OrderDTO;
import com.epam.rd.autocode.spring.project.model.Employee;
import com.epam.rd.autocode.spring.project.model.Order;
import com.epam.rd.autocode.spring.project.model.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Range;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o " +
            "WHERE LOWER(o.status) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR STR(o.price) LIKE CONCAT('%', :keyword, '%') " +
            "OR STR(o.orderDate) LIKE CONCAT('%', :keyword, '%')")
    Page<Order> searchOrders(@Param("keyword") String keyword, Pageable pageable);

    Optional<Order> getOrderById(Long orderId);

    Page<Order> findByClientId(Long clientId, Pageable pageable);

    Page<Order> findByEmployeeId(Long employeeId, Pageable pageable);

}
