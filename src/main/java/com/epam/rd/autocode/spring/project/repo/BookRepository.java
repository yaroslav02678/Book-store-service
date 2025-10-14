package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.dto.BookDTO;
import com.epam.rd.autocode.spring.project.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    @Query("SELECT b FROM Book b " +
            "WHERE LOWER(b.name) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "   OR LOWER(b.genre) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<Book> searchBooks(String query, Pageable pageable);

    Optional<Book> getBookById(long id);

    boolean existsByNameIgnoreCaseAndAuthorIgnoreCase(String name, String author);
}
