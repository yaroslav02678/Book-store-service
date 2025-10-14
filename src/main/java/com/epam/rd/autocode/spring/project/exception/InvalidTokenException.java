package com.epam.rd.autocode.spring.project.exception;

import lombok.Getter;

@Getter
public class InvalidTokenException extends RuntimeException {
    private final Object[] args;

    public InvalidTokenException(String messageKey, Object... args) {
        super(messageKey);
        this.args = args;
    }
}
