package com.epam.rd.autocode.spring.project.repo;

import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.Cart;
import com.epam.rd.autocode.spring.project.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndBook(Cart cart, Book book);
    void deleteByCartAndBook(Cart cart, Book book);

    @Query("SELECT ci FROM CartItem ci WHERE ci.cart.clientId = :userId AND ci.book.id = :bookId")
    Optional<CartItem> findByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);}

