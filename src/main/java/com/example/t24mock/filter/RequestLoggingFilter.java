package com.example.t24mock.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(wrappedRequest, wrappedResponse);

            String contentType = request.getHeader("Content-Type");
            String method = request.getMethod();
            String path = request.getRequestURI();
            byte[] bodyBytes = wrappedRequest.getContentAsByteArray();
            String body = bodyBytes.length > 0 ? new String(bodyBytes, StandardCharsets.UTF_8) : "";

            logger.info("========================================");
            logger.info("HTTP REQUEST");
            logger.info("Method      : {}", method);
            logger.info("Path        : {}", path);
            logger.info("Content-Type: {}", contentType);
            logger.info("Body        : {}", body);
            logger.info("========================================");
            logger.info("Response Status: {}", wrappedResponse.getStatus());

            wrappedResponse.copyBodyToResponse();
        } finally {
            wrappedResponse.copyBodyToResponse();
        }
    }
}
