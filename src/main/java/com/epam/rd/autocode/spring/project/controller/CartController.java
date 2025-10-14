package com.epam.rd.autocode.spring.project.controller;

import com.epam.rd.autocode.spring.project.aop.LoggableBusinessEvent;
import com.epam.rd.autocode.spring.project.model.CartItem;
import com.epam.rd.autocode.spring.project.service.CartService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add/{bookId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public String addToCart(@PathVariable Long bookId,
                            Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        cartService.addBookToCart(Long.valueOf(userId), bookId);
        return "redirect:/cart";
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CLIENT')")
    @LoggableBusinessEvent("Перегляд кошика користувача")
    public String viewCart(Authentication authentication, Model model) {
        String userId = (String) authentication.getPrincipal();
        List<CartItem> cartItems = cartService.getCart(Long.valueOf(userId));

        BigDecimal totalPrice = cartItems.stream()
                .map(item -> item.getBook().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);

        return "cart/cart-view";
    }

    @GetMapping("/remove/{bookId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public String removeFromCart(@PathVariable Long bookId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        cartService.removeBookFromCart(Long.valueOf(userId), bookId);
        return "redirect:/cart";
    }

    @PostMapping("/increase/{bookId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public String increaseQuantity(@PathVariable Long bookId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        cartService.increaseQuantity(Long.valueOf(userId), bookId);
        return "redirect:/cart";
    }

    @PostMapping("/decrease/{bookId}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public String decreaseQuantity(@PathVariable Long bookId, Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        cartService.decreaseQuantity(Long.valueOf(userId), bookId);
        return "redirect:/cart";
    }

    @GetMapping("/add/{bookId}")
    public String addToCartWithLogin(@PathVariable Long bookId,
                                     Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        cartService.addBookToCart(Long.valueOf(userId), bookId);
        return "redirect:/cart";
    }
}
