package com.epam.rd.autocode.spring.project.service.impl;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.Cart;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.CartItemRepository;
import com.epam.rd.autocode.spring.project.repo.CartRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           BookRepository bookRepository,
                           ClientRepository clientRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @LoggableBusinessEvent("Додавання книги до кошика")
    public void addBookToCart(Long userId, long bookId) {
        Client user = clientRepository.findClientById(userId)
                .orElseThrow(() -> new NotFoundException("error.user.notFound", userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("error.book.notFound", bookId));

        Cart cart = cartRepository.findByClientId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setClientId(userId);
                    return cartRepository.save(newCart);
                });

        CartItem item = cartItemRepository.findByCartAndBook(cart, book)
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setCart(cart);
                    newItem.setBook(book);
                    newItem.setQuantity(0);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);

        user.setBalance(user.getBalance().subtract(book.getPrice()));
        clientRepository.save(user);
    }


    @Override
    public List<CartItem> getCart(Long userId) {
        return cartRepository.findByClientId(userId)
                .map(Cart::getItems)
                .orElse(List.of());
    }

    @Override
    @LoggableBusinessEvent("Видалення книги з кошика")
    public void removeBookFromCart(Long userId, long bookId) {
        Cart cart = cartRepository.findByClientId(userId)
                .orElseThrow(() -> new NotFoundException("error.cart.notFound", userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("error.book.notFound", bookId));

        cartItemRepository.findByCartAndBook(cart, book)
                .ifPresent(cartItemRepository::delete);
    }

    public void increaseQuantity(Long userId, Long bookId) {
        CartItem item = cartItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));
        item.setQuantity(item.getQuantity() + 1);
        cartItemRepository.save(item);
    }

    public void decreaseQuantity(Long userId, Long bookId) {
        CartItem item = cartItemRepository.findByUserIdAndBookId(userId, bookId)
                .orElseThrow(() -> new RuntimeException("Book not found in cart"));

        if (item.getQuantity() > 1) {
            item.setQuantity(item.getQuantity() - 1);
            cartItemRepository.save(item);
        } else {
            cartItemRepository.delete(item);
        }
    }
}
