package com.mohit.filters;

import com.mohit.util.AuthConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(1) // Ensure this filter runs before others that might require authentication
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (authenticationHeader == null) {
            logger.info("No authentication header found, allowing request to proceed without authentication.");
            filterChain.doFilter(request, response);
            return;
        }
        logger.info(String.format("Authentication header found: %s", authenticationHeader));
        filterChain.doFilter(request, response);
    }
}
