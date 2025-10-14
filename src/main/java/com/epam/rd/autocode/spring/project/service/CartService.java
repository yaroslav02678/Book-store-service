package com.epam.rd.autocode.spring.project.service;

import com.epam.rd.autocode.spring.project.model.CartItem;

import java.util.List;

public interface CartService {

    void addBookToCart(Long userId, long bookId);

    List<CartItem> getCart(Long userId);

    void removeBookFromCart(Long userId, long bookId);

    void increaseQuantity(Long aLong, Long bookId);

    void decreaseQuantity(Long aLong, Long bookId);
}
