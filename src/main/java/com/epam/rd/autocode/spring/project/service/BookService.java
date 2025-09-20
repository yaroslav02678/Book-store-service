package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookService {

    Page<BookDTO> getAllBooks(Pageable pageable);

    BookDTO getBookByName(String name);

    Page<BookDTO> getBooksByAuthor(String author, Pageable pageable);

    Page<BookDTO> getBooksByGenre(String genre, Pageable pageable);

    BookDTO updateBookByName(String name, BookDTO book);

    void deleteBookByName(String name);

    BookDTO addBook(BookDTO book);
}
