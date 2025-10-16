package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.*;

@Service
public class GeminiService {

    private final WebClient webClient;
    private final BookRepository bookRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    public GeminiService(BookRepository bookRepository, WebClient.Builder webClientBuilder) {
        this.bookRepository = bookRepository;
        this.webClient = webClientBuilder.build();
    }

    public String askAssistant(String userMessage) {
        List<Book> books = bookRepository.findAll();
        String booksInfo = books.stream()
                .limit(10)
                .map(book -> String.format("Назва: %s, Автор: %s, Ціна: %.2f грн, Жанр: %s",
                        book.getName(), book.getAuthor(), book.getPrice(), book.getGenre()))
                .reduce("", (a,b) -> a + "\n" + b);

        String prompt = """
                Ти — асистент книжкового магазину.
                Ось список доступних книг:

                %s

                Користувач запитує: %s
                Порадь книгу, відповідай українською, не вигадуй назви.
                """.formatted(booksInfo, userMessage);

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", prompt)
                        ))
                )
        );

        try {
            Map<?, ?> response = webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("https")
                            .host("generativelanguage.googleapis.com")
                            .path("/v1beta/models/gemini-2.0-flash:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            List<?> candidates = (List<?>) response.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<?, ?> content = (Map<?, ?>) ((Map<?, ?>) candidates.get(0)).get("content");
                List<?> parts = (List<?>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    return ((Map<?, ?>) parts.get(0)).get("text").toString().trim();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Не вдалося отримати відповідь від асистента 😞";
    }
}


