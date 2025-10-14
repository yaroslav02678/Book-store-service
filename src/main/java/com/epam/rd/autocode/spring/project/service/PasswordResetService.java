package com.epam.rd.autocode.spring.project.service;

public interface PasswordResetService {
    void initiatePasswordReset(String email);
    void validatePasswordResetToken(String token);
    void completePasswordReset(String token, String newPassword);
}
