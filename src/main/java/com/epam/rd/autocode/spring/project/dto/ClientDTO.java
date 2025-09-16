package com.epam.rd.autocode.spring.project.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
public class ClientDTO{
    private String email;
    private String password;
    private String name;
    private BigDecimal balance;

    public ClientDTO() {}

    public ClientDTO(String email,
                     String password,
                     String name,
                     BigDecimal balance) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.balance = balance;
    }
}
