package com.epam.rd.autocode.spring.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyExistException extends RuntimeException {
    private final Object[] args;

    public AlreadyExistException(String messageKey, Object... args) {
        super(messageKey);
        this.args = args;
    }
}