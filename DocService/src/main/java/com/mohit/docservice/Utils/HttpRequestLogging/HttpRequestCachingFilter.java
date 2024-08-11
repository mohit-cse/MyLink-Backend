package com.mohit.docservice.Utils.HttpRequestLogging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
public class HttpRequestCachingFilter extends OncePerRequestFilter {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpRequestCachingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
//        LOGGER.info("REQUEST DATA: " + IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8));
        filterChain.doFilter(cachedHttpServletRequest, response);
        LOGGER.info("Request: {} {} {}", request.getMethod(), response.getStatus(), request.getRequestURI());
    }
}
