package com.epam.rd.autocode.spring.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Value("${jwt.access-token-expiration-seconds}")
    private long accessTokenExpirationSeconds;

    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        Optional<String> accessToken = getTokenFromCookie(request, "JWT");
        if (accessToken.isPresent() && jwtUtils.validateToken(accessToken.get())) {
            authenticateWithToken(accessToken.get());
        } else {
            Optional<String> refreshToken = getTokenFromCookie(request, "RefreshJWT");
            refreshToken.ifPresent(token -> tryToRefreshAuthentication(token, response));
        }

        filterChain.doFilter(request, response);
    }

    private void tryToRefreshAuthentication(String refreshToken, HttpServletResponse response) {
        if (!jwtUtils.validateToken(refreshToken)) {
            return;
        }

        String role = jwtUtils.getRole(refreshToken);
        String subject = jwtUtils.getSubject(refreshToken);
        String email = jwtUtils.getEmail(refreshToken);
        String newAccessToken = jwtUtils.generateAccessToken(subject, role, email);

        Cookie newAccessTokenCookie = new Cookie("JWT", newAccessToken);
        newAccessTokenCookie.setHttpOnly(true);
        newAccessTokenCookie.setPath("/");
        newAccessTokenCookie.setMaxAge((int) accessTokenExpirationSeconds);
        response.addCookie(newAccessTokenCookie);

        authenticateWithToken(refreshToken);
    }

    private void authenticateWithToken(String token) {
        try {
            String role = jwtUtils.getRole(token);
            String subject = jwtUtils.getSubject(token);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    subject,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority(role))
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }
    }

    private Optional<String> getTokenFromCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
    }
}