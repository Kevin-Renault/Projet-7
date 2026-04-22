package com.openclassroom.devops.orion.microcrm;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
class EndpointRequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(EndpointRequestLoggingFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String requestUri = request.getRequestURI();
        return requestUri.startsWith("/actuator/health") || requestUri.startsWith("/api/telemetry/front-logs");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestPath = request.getRequestURI();
        String queryString = request.getQueryString();
        String pathWithQuery = queryString == null ? requestPath : requestPath + "?" + queryString;
        String httpMethod = request.getMethod();
        long startTime = System.nanoTime();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime);
            int status = response.getStatus();
            log.info("http_request",
                    StructuredArguments.kv("http_method", httpMethod),
                    StructuredArguments.kv("http_path", pathWithQuery),
                    StructuredArguments.kv("http_status", status),
                    StructuredArguments.kv("http_duration_ms", durationMs));
        }
    }
}