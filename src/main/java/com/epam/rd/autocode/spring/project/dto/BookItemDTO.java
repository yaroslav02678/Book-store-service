package com.epam.rd.autocode.spring.project.dto;


import lombok.*;

@Getter
@Setter
public class BookItemDTO {
    private String bookName;
    private Integer quantity;

    public BookItemDTO() {}

    public BookItemDTO(String bookName,
                       Integer quantity) {
        this.bookName = bookName;
        this.quantity = quantity;
    }
}
