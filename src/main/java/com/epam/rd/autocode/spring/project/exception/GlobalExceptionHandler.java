package com.epam.rd.autocode.spring.project.exception;

import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(AlreadyExistException.class)
    public String handleAlreadyExist(AlreadyExistException ex, Model model, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        model.addAttribute("errorMessage", errorMessage);
        return "error/409";
    }

    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model, Locale locale) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getArgs(), locale);
        model.addAttribute("errorMessage", errorMessage);
        return "error/404";
    }
}

