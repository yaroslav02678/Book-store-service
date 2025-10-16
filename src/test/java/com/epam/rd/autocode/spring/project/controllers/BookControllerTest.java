package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.enums.AgeGroup;
import com.epam.rd.autocode.spring.project.model.enums.Language;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// FIX: Додано властивість spring.messages.basename
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    private BookDTO sampleBook;
    private Page<BookDTO> bookPage;

    @BeforeEach
    void setUp() {
        sampleBook = new BookDTO();
        sampleBook.setId(1L);
        sampleBook.setName("Test Book");
        sampleBook.setGenre("Fiction");
        sampleBook.setAgeGroup(AgeGroup.ADULT);
        sampleBook.setPrice(new BigDecimal("25.99"));
        sampleBook.setPublicationDate(LocalDate.now().minusYears(1));
        sampleBook.setAuthor("Test Author");
        sampleBook.setPages(300);
        sampleBook.setCharacteristics("This is a set of valid book characteristics.");
        sampleBook.setDescription("This is a valid and sufficiently long book description.");
        sampleBook.setLanguage(Language.ENGLISH);
        sampleBook.setImageUrl("https://example.com/image.jpg");

        List<BookDTO> bookList = Collections.singletonList(sampleBook);
        bookPage = new PageImpl<>(bookList, PageRequest.of(0, 6), 1);
    }

    @Test
    void books_withAscendingSort_shouldUseAscendingSortOrder() throws Exception {
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/books")
                        .param("sortField", "name")
                        .param("sortDir", "asc")) // Явно вказуємо напрямок сортування
                .andExpect(status().isOk());

        verify(bookService).getAllBooks(pageableCaptor.capture());
        Pageable capturedPageable = pageableCaptor.getValue();

        assertThat(capturedPageable.getSort().getOrderFor("name").getDirection())
                .isEqualTo(Sort.Direction.ASC);
    }

    @Test
    void books_shouldReturnBookListView() throws Exception {
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "6")
                        .param("sortField", "name")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-list"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void searchBooks_shouldReturnBookSearchView() throws Exception {
        when(bookService.searchBooks(anyString(), any(Pageable.class))).thenReturn(bookPage);

        mockMvc.perform(get("/books/search").param("q", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-search"))
                .andExpect(model().attributeExists("books"));
    }

    @Test
    void searchBooks_withDescendingSort_shouldUseDescendingSortOrder() throws Exception {
        when(bookService.searchBooks(anyString(), any(Pageable.class))).thenReturn(bookPage);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);

        mockMvc.perform(get("/books/search")
                        .param("q", "test")
                        .param("sortDir", "desc"))
                .andExpect(status().isOk());

        verify(bookService).searchBooks(anyString(), pageableCaptor.capture());
        Pageable pageable = pageableCaptor.getValue();
        assertThat(pageable.getSort().getOrderFor("name").getDirection()).isEqualTo(Sort.Direction.DESC);
    }

    @Test
    void detail_shouldReturnBookDetailView() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);

        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-detail"))
                .andExpect(model().attribute("book", sampleBook));
    }

    // --- Edit Book Tests (EMPLOYEE, ADMIN) ---

    @Test
    void showEditForm_whenAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/edit/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void showEditForm_whenRoleClient_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/books/edit/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void showEditForm_whenRoleEmployee_shouldSucceed() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);
        mockMvc.perform(get("/books/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-edit"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateBook_withValidData_shouldUpdateAndRedirect() throws Exception {
        when(bookService.updateBookById(anyLong(), any(BookDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/books/edit/1")
                        .with(csrf())
                        .flashAttr("book", sampleBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/books?*"));

        verify(bookService, times(1)).updateBookById(anyLong(), any(BookDTO.class));
    }

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void updateBook_withInvalidData_shouldReturnEditView() throws Exception {
        sampleBook.setName(""); // Invalid state

        mockMvc.perform(post("/books/edit/1")
                        .with(csrf())
                        .flashAttr("book", sampleBook))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-edit"));

        verify(bookService, never()).updateBookById(anyLong(), any(BookDTO.class));
    }

    // --- Add Book Tests (ADMIN only) ---

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void showAddForm_whenRoleEmployee_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void showAddForm_whenRoleAdmin_shouldSucceed() throws Exception {
        mockMvc.perform(get("/books/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-add"))
                .andExpect(model().attributeExists("book"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook_withValidData_shouldAddAndRedirect() throws Exception {
        when(bookService.addBook(any(BookDTO.class))).thenReturn(sampleBook);

        mockMvc.perform(post("/books/add")
                        .with(csrf())
                        .flashAttr("book", sampleBook))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/books?*"));

        verify(bookService, times(1)).addBook(any(BookDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addBook_withInvalidData_shouldReturnAddView() throws Exception {
        sampleBook.setAuthor(""); // Invalid state

        mockMvc.perform(post("/books/add")
                        .with(csrf())
                        .flashAttr("book", sampleBook))
                .andExpect(status().isOk())
                .andExpect(view().name("books/book-add"));

        verify(bookService, never()).addBook(any(BookDTO.class));
    }

    // --- Delete Book Tests (ADMIN only) ---

    @Test
    @WithMockUser(roles = "EMPLOYEE")
    void deleteBook_whenRoleEmployee_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/books/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    void deleteBook_whenRoleClient_shouldBeForbidden() throws Exception {
        mockMvc.perform(get("/books/delete/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBook_whenAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/books/delete/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteBook_whenRoleAdmin_shouldDeleteAndRedirect() throws Exception {
        doNothing().when(bookService).deleteBookById(1L);

        mockMvc.perform(get("/books/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/books?*"));

        verify(bookService, times(1)).deleteBookById(1L);
    }
}