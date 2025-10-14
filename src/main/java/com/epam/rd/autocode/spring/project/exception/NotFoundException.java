package com.epam.rd.autocode.spring.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
public class NotFoundException extends RuntimeException {
    private final Object[] args;

    public NotFoundException(String messageKey, Object... args) {
        super(messageKey);
        this.args = args;
    }
}
