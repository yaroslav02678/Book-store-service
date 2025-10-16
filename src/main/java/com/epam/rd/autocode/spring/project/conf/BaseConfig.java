package com.epam.rd.autocode.spring.project.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BaseConfig implements WebMvcConfigurer {
    @GetMapping("/")
    public String redirectToBooks() {
        return "redirect:/books";
    }
}
