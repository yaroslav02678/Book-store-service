package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.exception.AlreadyExistException;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookServiceImpl bookServiceImpl;

    private Book book;
    private BookDTO bookDTO;

    @Test
    void getAllBooks_whenBooksExist_returnsCorrectlyMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(Collections.singletonList(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        Page<BookDTO> result = bookServiceImpl.getAllBooks(pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals("Test Book", result.getContent().get(0).getName());
        verify(bookRepository).findAll(pageable);
        verify(modelMapper).map(book, BookDTO.class);
    }

    @Test
    void getAllBooks_whenNoBooksExist_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);
        when(bookRepository.findAll(pageable)).thenReturn(Page.empty());
        Page<BookDTO> result = bookServiceImpl.getAllBooks(pageable);
        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void getBookById_whenBookExists_returnsBookDTO() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);
        BookDTO result = bookServiceImpl.getBookById(1L);
        assertNotNull(result);
        assertEquals(bookDTO.getName(), result.getName());
    }

    @Test
    void getBookById_whenBookNotFound_throwsNotFoundException() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> bookServiceImpl.getBookById(99L));
    }

    @Test
    void addBook_whenBookIsNew_returnsSavedBookDTO() {
        when(bookRepository.existsByNameIgnoreCaseAndAuthorIgnoreCase(bookDTO.getName(), bookDTO.getAuthor())).thenReturn(false);
        when(modelMapper.map(bookDTO, Book.class)).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        BookDTO result = bookServiceImpl.addBook(bookDTO);

        assertNotNull(result);
        assertEquals(bookDTO.getName(), result.getName());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void addBook_whenBookAlreadyExists_throwsAlreadyExistException() {
        when(bookRepository.existsByNameIgnoreCaseAndAuthorIgnoreCase(bookDTO.getName(), bookDTO.getAuthor())).thenReturn(true);

        assertThrows(AlreadyExistException.class, () -> bookServiceImpl.addBook(bookDTO));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void updateBookById_whenBookNotFound_throwsNotFoundException() {
        long bookId = 99L;

        assertThrows(NotFoundException.class,
                () -> bookServiceImpl.updateBookById(bookId, bookDTO));

        verify(bookRepository, never()).save(any());
    }


    @Test
    void deleteBookById_whenBookExists_deletesBook() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookServiceImpl.deleteBookById(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBookById_whenBookNotFound_throwsNotFoundException() {
        when(bookRepository.existsById(99L)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> bookServiceImpl.deleteBookById(99L));
        verify(bookRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchBooks_whenBooksFound_returnsMappedPage() {
        Pageable pageable = PageRequest.of(0, 5);
        String keyword = "java";
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.searchBooks(keyword, pageable)).thenReturn(bookPage);
        when(modelMapper.map(book, BookDTO.class)).thenReturn(bookDTO);

        Page<BookDTO> result = bookServiceImpl.searchBooks(keyword, pageable);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        assertEquals(bookDTO.getName(), result.getContent().get(0).getName());
        verify(bookRepository).searchBooks(keyword, pageable);
    }

    @Test
    void searchBooks_whenNoBooksFound_returnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 5);
        String keyword = "nonexistent";
        when(bookRepository.searchBooks(keyword, pageable)).thenReturn(Page.empty());

        Page<BookDTO> result = bookServiceImpl.searchBooks(keyword, pageable);

        assertTrue(result.isEmpty());
        verify(modelMapper, never()).map(any(), any());
    }

    @Test
    void updateBookById_whenBookExists_mapsUpdatesAndSaves() {
        long bookId = 1L;

        BookDTO updateDto = new BookDTO();
        updateDto.setName("New Name");

        Book existingBook = new Book();
        existingBook.setId(bookId);
        BookDTO finalReturnDto = new BookDTO();
        when(bookRepository.getBookById(bookId)).thenReturn(Optional.of(existingBook));
        doNothing().when(modelMapper).map(updateDto, existingBook);
        when(bookRepository.save(existingBook)).thenReturn(existingBook);
        when(modelMapper.map(existingBook, BookDTO.class)).thenReturn(finalReturnDto);
        BookDTO result = bookServiceImpl.updateBookById(bookId, updateDto);
        assertNotNull(result);
        verify(modelMapper).map(updateDto, existingBook);
        verify(bookRepository).save(existingBook);
        verify(modelMapper).map(existingBook, BookDTO.class);
    }

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setName("Test Book");
        book.setAuthor("Test Author");

        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setName("Test Book");
        bookDTO.setAuthor("Test Author");
    }
}