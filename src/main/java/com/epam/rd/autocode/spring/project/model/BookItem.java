package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BookItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;
    private Integer quantity;

    public BookItem() {}

    public BookItem(Long id,
                    Order order,
                    Book book,
                    Integer quantity) {
        this.id = id;
        this.order = order;
        this.book = book;
        this.quantity = quantity;
    }
}
