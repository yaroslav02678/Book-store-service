package com.epam.rd.autocode.spring.project.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@Table(name = "clients")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class Client extends User {

    @Column(name = "balance")
    private BigDecimal balance;

    public Client(Long id, String email, String password, String name, BigDecimal balance) {
        super(id, email, password, name);
        this.balance = balance;
    }
}

