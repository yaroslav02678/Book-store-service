package com.epam.rd.autocode.spring.project.service;

import org.springframework.scheduling.annotation.Async;

public interface EmailService {
    @Async
    void sendPasswordResetEmail(String to, String token);

    void sendEmail(String to, String subject, String text);
}
