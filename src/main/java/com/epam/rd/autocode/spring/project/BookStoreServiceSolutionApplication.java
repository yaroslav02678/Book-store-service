package com.epam.rd.autocode.spring.project;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookStoreServiceSolutionApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();

        System.setProperty("SERVER_PORT", dotenv.get("SERVER_PORT"));
        System.setProperty("DB_URL", dotenv.get("DB_URL"));
        System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
        System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
        System.setProperty("JPA_HIBERNATE_DDL_AUTO", dotenv.get("JPA_HIBERNATE_DDL_AUTO"));
        System.setProperty("JPA_SHOW_SQL", dotenv.get("JPA_SHOW_SQL"));
        System.setProperty("JPA_FORMAT_SQL", dotenv.get("JPA_FORMAT_SQL"));
        System.setProperty("SQL_INIT_MODE", dotenv.get("SQL_INIT_MODE"));
        System.setProperty("SQL_INIT_SCHEMA", dotenv.get("SQL_INIT_SCHEMA"));
        System.setProperty("SQL_INIT_DATA", dotenv.get("SQL_INIT_DATA"));
        System.setProperty("TEMPLATES_PREFIX", dotenv.get("TEMPLATES_PREFIX"));
        System.setProperty("TEMPLATES_SUFFIX", dotenv.get("TEMPLATES_SUFFIX"));
        System.setProperty("MESSAGES_BASENAME", dotenv.get("MESSAGES_BASENAME"));
        System.setProperty("MESSAGES_ENCODING", dotenv.get("MESSAGES_ENCODING"));
        System.setProperty("JWT_SECRET", dotenv.get("JWT_SECRET"));
        System.setProperty("JWT_ACCESS_TOKEN_EXPIRATION", dotenv.get("JWT_ACCESS_TOKEN_EXPIRATION"));
        System.setProperty("JWT_REFRESH_TOKEN_EXPIRATION", dotenv.get("JWT_REFRESH_TOKEN_EXPIRATION"));
        System.setProperty("MAIL_HOST", dotenv.get("MAIL_HOST"));
        System.setProperty("MAIL_PORT", dotenv.get("MAIL_PORT"));
        System.setProperty("MAIL_USERNAME", dotenv.get("MAIL_USERNAME"));
        System.setProperty("MAIL_PASSWORD", dotenv.get("MAIL_PASSWORD"));
        System.setProperty("MAIL_SMTP_AUTH", dotenv.get("MAIL_SMTP_AUTH"));
        System.setProperty("MAIL_STARTTLS_ENABLE", dotenv.get("MAIL_STARTTLS_ENABLE"));

        SpringApplication.run(BookStoreServiceSolutionApplication.class, args);
    }

}
