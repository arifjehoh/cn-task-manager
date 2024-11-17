package com.github.arifjehoh.taskmanager.configurations.web;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class JwtLoginFilter implements Filter {

    private final JwtDecoder jwtDecoder;

    public JwtLoginFilter(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String authorizationHeader = ((HttpServletRequest) request).getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new ServletException("Unauthorized");
        }
        String token = authorizationHeader.substring(7);
        try {
            Jwt decodedToken = jwtDecoder.decode(token);
            if (Objects.requireNonNull(decodedToken.getExpiresAt())
                       .isBefore(Instant.now())) {
                throw new ServletException("Unauthorized");
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(decodedToken.getSubject(), null, List.of());
            SecurityContextHolder.getContext()
                                 .setAuthentication(usernamePasswordAuthenticationToken);
        } catch (Exception e) {
            throw new ServletException("Unauthorized");
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
