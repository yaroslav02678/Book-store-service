package com.epam.rd.autocode.spring.project.controllers;

import com.epam.rd.autocode.spring.project.model.Book;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    private final Authentication clientAuthentication = new UsernamePasswordAuthenticationToken(
            "1",
            null,
            AuthorityUtils.createAuthorityList("ROLE_CLIENT")
    );

    private final Authentication adminAuthentication = new UsernamePasswordAuthenticationToken(
            "2",
            null,
            AuthorityUtils.createAuthorityList("ROLE_ADMIN")
    );

    @Test
    void addToCart_withValidClient_coversServiceCall() throws Exception {
        Long userId = 1L;
        Long bookId = 10L;
        doNothing().when(cartService).addBookToCart(userId, bookId);

        mockMvc.perform(post("/cart/add/{bookId}", bookId)
                        .with(authentication(clientAuthentication))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).addBookToCart(userId, bookId);
    }

    @Test
    void viewCart_withValidClient_coversServiceCallAndCalculation() throws Exception {
        Long userId = 1L;
        Book book = new Book();
        book.setPrice(new BigDecimal("20.50"));
        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(2);
        List<CartItem> cartItems = Collections.singletonList(item);

        when(cartService.getCart(userId)).thenReturn(cartItems);

        mockMvc.perform(get("/cart")
                        .with(authentication(clientAuthentication)))
                .andExpect(status().isOk())
                .andExpect(view().name("cart/cart-view"))
                .andExpect(model().attribute("cartItems", cartItems))
                .andExpect(model().attribute("totalPrice", new BigDecimal("41.00")));

        verify(cartService).getCart(userId);
    }

    @Test
    void removeFromCart_withValidClient_coversServiceCall() throws Exception {
        Long userId = 1L;
        Long bookId = 10L;
        doNothing().when(cartService).removeBookFromCart(userId, bookId);

        mockMvc.perform(get("/cart/remove/{bookId}", bookId)
                        .with(authentication(clientAuthentication)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).removeBookFromCart(userId, bookId);
    }

    @Test
    void increaseQuantity_withValidClient_coversServiceCall() throws Exception {
        Long userId = 1L;
        Long bookId = 10L;
        doNothing().when(cartService).increaseQuantity(userId, bookId);

        mockMvc.perform(post("/cart/increase/{bookId}", bookId)
                        .with(authentication(clientAuthentication))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).increaseQuantity(userId, bookId);
    }

    @Test
    void decreaseQuantity_withValidClient_coversServiceCall() throws Exception {
        Long userId = 1L;
        Long bookId = 10L;
        doNothing().when(cartService).decreaseQuantity(userId, bookId);

        mockMvc.perform(post("/cart/decrease/{bookId}", bookId)
                        .with(authentication(clientAuthentication))
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).decreaseQuantity(userId, bookId);
    }

    @Test
    void addToCartWithLogin_withValidClient_coversServiceCall() throws Exception {
        Long userId = 1L;
        Long bookId = 10L;
        doNothing().when(cartService).addBookToCart(userId, bookId);

        mockMvc.perform(get("/cart/add/{bookId}", bookId)
                        .with(authentication(clientAuthentication)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        verify(cartService).addBookToCart(userId, bookId);
    }

    @Test
    void addToCart_whenAnonymous_shouldRedirectToLogin() throws Exception {
        mockMvc.perform(post("/cart/add/1").with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addToCart_withWrongRole_shouldBeForbidden() throws Exception {
        mockMvc.perform(post("/cart/add/1")
                        .with(authentication(adminAuthentication))
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}