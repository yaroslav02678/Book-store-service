package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.BookService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<BookDTO> getAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> modelMapper.map(book, BookDTO.class)
        );
    }

    @Override
    public BookDTO getBookById(long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("error.book.notFound", id));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public Page<BookDTO> searchBooks(String keyword, Pageable pageable) {
        return bookRepository.searchBooks(keyword, pageable)
                .map(book -> modelMapper.map(book, BookDTO.class));
    }

    @Override
    @LoggableBusinessEvent("Оновлення книги за ID")
    public BookDTO updateBookById(long id, BookDTO bookDTO) {
        Book book = bookRepository.getBookById(id)
                .orElseThrow(() -> new NotFoundException("error.book.notFound", id));

        modelMapper.map(bookDTO, book);

        bookRepository.save(book);
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    @LoggableBusinessEvent("Видалення книги за ID")
    public void deleteBookById(long id) {
        if (!bookRepository.existsById(id)) {
            throw new NotFoundException("error.book.notFound", id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    @LoggableBusinessEvent("Додавання нової книги")
    public BookDTO addBook(BookDTO bookDTO) {
        if(bookRepository.existsByNameIgnoreCaseAndAuthorIgnoreCase(bookDTO.getName(), bookDTO.getAuthor())) {
            throw new AlreadyExistException("error.book.alreadyExists", bookDTO.getName());
        }

        Book entity = modelMapper.map(bookDTO, Book.class);
        Book savedEntity = bookRepository.save(entity);
        return modelMapper.map(savedEntity, BookDTO.class);
    }

    @Override
    public List<BookDTO> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return Collections.singletonList(modelMapper.map(books, BookDTO.class));
    }
}

