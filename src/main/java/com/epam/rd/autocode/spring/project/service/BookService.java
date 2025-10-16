package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    Page<BookDTO> getAllBooks(Pageable pageable);

    BookDTO getBookById(long id);

    Page<BookDTO> searchBooks(String keyword, Pageable pageable);

    BookDTO updateBookById(long id, BookDTO bookDTO);

    void deleteBookById(long id);

    BookDTO addBook(BookDTO bookDTO);

    List<BookDTO> findAllBooks();
}
