package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO getBookByName(String name) {
        Book book = bookRepository.findBookByName(name)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return mapToDTO(book);
    }

    @Override
    public BookDTO updateBookByName(String name, BookDTO bookDTO) {
        Book book = bookRepository.findBookByName(name)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        book.setName(bookDTO.getName());
        book.setGenre(bookDTO.getGenre());
        book.setAgeGroup(bookDTO.getAgeGroup());
        book.setPrice(bookDTO.getPrice());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setAuthor(bookDTO.getAuthor());
        book.setPages(bookDTO.getPages());
        book.setCharacteristics(bookDTO.getCharacteristics());
        book.setDescription(bookDTO.getDescription());
        book.setLanguage(bookDTO.getLanguage());

        Book savedBook = bookRepository.save(book);
        return mapToDTO(savedBook);
    }

    @Override
    public void deleteBookByName(String name) {
        Book book = bookRepository.findBookByName(name)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        bookRepository.delete(book);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setName(bookDTO.getName());
        book.setGenre(bookDTO.getGenre());
        book.setAgeGroup(bookDTO.getAgeGroup());
        book.setPrice(bookDTO.getPrice());
        book.setPublicationDate(bookDTO.getPublicationDate());
        book.setAuthor(bookDTO.getAuthor());
        book.setPages(bookDTO.getPages());
        book.setCharacteristics(bookDTO.getCharacteristics());
        book.setDescription(bookDTO.getDescription());
        book.setLanguage(bookDTO.getLanguage());

        Book savedBook = bookRepository.save(book);
        return mapToDTO(savedBook);
    }

    private BookDTO mapToDTO(Book book) {
        return new BookDTO(
                book.getName(),
                book.getGenre(),
                book.getAgeGroup(),
                book.getPrice(),
                book.getPublicationDate(),
                book.getAuthor(),
                book.getPages(),
                book.getCharacteristics(),
                book.getDescription(),
                book.getLanguage()
        );
    }
}

