package com.godsaeng.godsaeng_up.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String requestId = UUID.randomUUID().toString();

        MDC.put(REQUEST_ID, requestId);

        long start = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        }finally {
            long end = System.currentTimeMillis();
            long took = end - start;

            String method = request.getMethod();
            String uri = request.getRequestURI();
            int status = response.getStatus();

            if (status >= 500) {
                log.error("[{}] {} {} -> status={} took={}ms",
                        requestId, method, uri, status, took);
            } else if (status >= 400) {
                log.warn("[{}] {} {} -> status={} took={}ms",
                        requestId, method, uri, status, took);
            } else {
                log.info("[{}] {} {} -> status={} took={}ms",
                        requestId, method, uri, status, took);
            }

            MDC.clear();
        }
    }
}
