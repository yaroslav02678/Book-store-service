package com.epam.rd.autocode.spring.project.security;

import com.epam.rd.autocode.spring.project.model.Client;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
public class PasswordResetToken {

    private static final int EXPIRATION_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = Client.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "client_id")
    private Client client;

    @Column(nullable = false)
    private Date expiryDate;

    public PasswordResetToken(String token, Client client) {
        this.token = token;
        this.client = client;
        this.expiryDate = calculateExpiryDate();
    }

    private Date calculateExpiryDate() {
        Instant now = Instant.now();
        Instant expiryInstant = now.plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES);
        return Date.from(expiryInstant);
    }

    public boolean isExpired() {
        return new Date().after(this.expiryDate);
    }
}