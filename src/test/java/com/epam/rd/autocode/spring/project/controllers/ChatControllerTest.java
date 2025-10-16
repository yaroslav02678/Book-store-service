package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.service.BookService;
import com.epam.rd.autocode.spring.project.service.impl.GeminiService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeminiService geminiService;

    @MockBean
    private BookService bookService;

    @MockBean
    private ModelMapper modelMapper;

    @Test
    void askGemini_shouldReturnBookListWithAnswer() throws Exception {
        Page<BookDTO> bookPage = new PageImpl<>(Collections.emptyList());
        when(geminiService.askAssistant(anyString())).thenReturn("This is a test answer.");
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(post("/ask").with(csrf()).param("message", "Hello"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-list"))
                .andExpect(model().attribute("userMessage", "Hello"))
                .andExpect(model().attribute("assistantAnswer", "This is a test answer."))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void askGemini_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        Page<BookDTO> bookPage = new PageImpl<>(Collections.emptyList());
        when(geminiService.askAssistant(anyString())).thenReturn("This is a test answer.");
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(post("/ask").with(csrf())
                        .param("message", "Hello")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk());

        verify(bookService).getAllBooks(pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }
}
