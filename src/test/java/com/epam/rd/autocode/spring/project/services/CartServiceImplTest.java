package com.epam.rd.autocode.spring.project.services;

import com.epam.rd.autocode.spring.project.dto.ClientDTO;
import com.epam.rd.autocode.spring.project.exception.NotFoundException;
import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.Cart;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.model.Client;
import com.epam.rd.autocode.spring.project.repo.BookRepository;
import com.epam.rd.autocode.spring.project.repo.CartItemRepository;
import com.epam.rd.autocode.spring.project.repo.CartRepository;
import com.epam.rd.autocode.spring.project.repo.ClientRepository;
import com.epam.rd.autocode.spring.project.service.ClientService;
import com.epam.rd.autocode.spring.project.service.impl.CartServiceImpl;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientService clientService;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CartServiceImpl cartService;

    private Client client;
    private Book book;
    private Cart cart;

    @Test
    void addBookToCart_whenUserAndBookExist_andCartIsNew_createsCartAndItem() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(cartItemRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());

        cartService.addBookToCart(1L, 10L);

        verify(cartRepository).save(any(Cart.class));
        verify(cartItemRepository).save(any(CartItem.class));
        verify(clientRepository).save(client);
        assertEquals(new BigDecimal("75.00"), client.getBalance());
    }

    @Test
    void addBookToCart_whenItemIsNewInExistingCart_createsNewItem() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndBook(cart, book)).thenReturn(Optional.empty());

        cartService.addBookToCart(1L, 10L);

        verify(cartItemRepository).save(argThat(item -> item.getQuantity() == 1));
        verify(clientRepository).save(client);
    }

    @Test
    void addBookToCart_whenItemExists_increasesQuantity() {
        CartItem existingItem = new CartItem();
        existingItem.setQuantity(1);
        existingItem.setCart(cart);
        existingItem.setBook(book);

        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(existingItem));

        cartService.addBookToCart(1L, 10L);

        verify(cartItemRepository).save(existingItem);
        assertEquals(2, existingItem.getQuantity());
    }

    @Test
    void addBookToCart_whenUserNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.addBookToCart(99L, 10L));
    }

    @Test
    void addBookToCart_whenBookNotFound_throwsNotFoundException() {
        when(clientRepository.findClientById(1L)).thenReturn(Optional.of(client));
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.addBookToCart(1L, 99L));
    }

    @Test
    void getCart_whenCartExists_returnsItems() {
        CartItem item = new CartItem();
        cart.setItems(List.of(item));
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.of(cart));

        List<CartItem> result = cartService.getCart(1L);

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
    }

    @Test
    void getCart_whenCartNotExists_returnsEmptyList() {
        when(cartRepository.findByClientId(99L)).thenReturn(Optional.empty());
        List<CartItem> result = cartService.getCart(99L);
        assertTrue(result.isEmpty());
    }

    @Test
    void removeBookFromCart_whenItemExists_deletesItem() {
        CartItem item = new CartItem();
        when(cartRepository.findByClientId(1L)).thenReturn(Optional.of(cart));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByCartAndBook(cart, book)).thenReturn(Optional.of(item));

        cartService.removeBookFromCart(1L, 10L);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void removeBookFromCart_whenCartNotFound_throwsNotFoundException() {
        when(cartRepository.findByClientId(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> cartService.removeBookFromCart(99L, 10L));
    }

    @Test
    void decreaseQuantity_whenQuantityIsGreaterThanOne_decreasesQuantity() {
        CartItem item = new CartItem();
        item.setQuantity(5);
        when(cartItemRepository.findByUserIdAndBookId(1L, 10L)).thenReturn(Optional.of(item));

        cartService.decreaseQuantity(1L, 10L);

        verify(cartItemRepository).save(item);
        assertEquals(4, item.getQuantity());
    }

    @Test
    void decreaseQuantity_whenQuantityIsOne_deletesItem() {
        CartItem item = new CartItem();
        item.setQuantity(1);
        when(cartItemRepository.findByUserIdAndBookId(1L, 10L)).thenReturn(Optional.of(item));

        cartService.decreaseQuantity(1L, 10L);

        verify(cartItemRepository).delete(item);
        verify(cartItemRepository, never()).save(any());
    }

    @Test
    void increaseQuantity_whenItemExists_incrementsQuantityAndSaves() {
        Long userId = 1L;
        Long bookId = 1L;

        CartItem cartItem = new CartItem();
        cartItem.setQuantity(2);

        when(cartItemRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(Optional.of(cartItem));
        cartService.increaseQuantity(userId, bookId);
        verify(cartItemRepository).save(cartItem);
        assertEquals(3, cartItem.getQuantity());
    }

    @Test
    void increaseQuantity_whenItemDoesNotExist_throwsRuntimeException() {
        Long userId = 99L;
        Long bookId = 99L;

        when(cartItemRepository.findByUserIdAndBookId(userId, bookId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.increaseQuantity(userId, bookId));

        assertEquals("Book not found in cart", exception.getMessage());

        verify(cartItemRepository, never()).save(any());
    }

    @BeforeEach
    void setUp() {
        client = new Client();
        client.setId(1L);
        client.setBalance(new BigDecimal("100.00"));

        book = new Book();
        book.setId(10L);
        book.setPrice(new BigDecimal("25.00"));

        cart = new Cart();
        cart.setCartId(5L);
        cart.setClientId(1L);
        cart.setItems(new ArrayList<>());
    }
}