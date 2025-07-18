package com.mohit.filters;

import com.mohit.models.authentication.AuthUser;
import com.mohit.models.authentication.Role;
import com.mohit.authentication.UserAuthentication;
import com.mohit.util.AuthConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Order(1) // Ensure this filter runs before others that might require authentication
public class AuthenticationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authenticationHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        if (authenticationHeader == null) {
            logger.info("No authentication header found, allowing request to proceed without authentication.");
            UserAuthentication authentication = new UserAuthentication(AuthUser.builder()
                    .isAuthenticated(true)
                    .name("Test User")
                    .roles(List.of(Role.builder().roleName("TEST_ROLE").build())).build());

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
            filterChain.doFilter(request, response);
            return;
        }
        logger.info(String.format("Authentication header found: %s", authenticationHeader));
        filterChain.doFilter(request, response);
    }
}
