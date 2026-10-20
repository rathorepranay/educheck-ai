
package com.educheck.api_gateway.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.UUID;

@Component
public class CorrelationFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(CorrelationFilter.class);

    private static final String HEADER = "X-Correlation-ID";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String correlationId = request.getHeader(HEADER);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        final String id = correlationId;

        HttpServletRequestWrapper wrappedRequest =
                new HttpServletRequestWrapper(request) {

                    @Override
                    public String getHeader(String name) {
                        if (HEADER.equalsIgnoreCase(name)) {
                            return id;
                        }
                        return super.getHeader(name);
                    }

                    @Override
                    public Enumeration<String> getHeaders(String name) {
                        if (HEADER.equalsIgnoreCase(name)) {
                            return Collections.enumeration(
                                    Collections.singletonList(id));
                        }
                        return super.getHeaders(name);
                    }

                    @Override
                    public Enumeration<String> getHeaderNames() {
                        var names = Collections.list(super.getHeaderNames());

                        if (names.stream().noneMatch(
                                name -> HEADER.equalsIgnoreCase(name))) {
                            names.add(HEADER);
                        }

                        return Collections.enumeration(names);
                    }
                };

        response.setHeader(HEADER, id);

        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(wrappedRequest, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            log.info(
                    "correlationId={} method={} path={} status={} durationMs={}",
                    id,
                    request.getMethod(),
                    request.getRequestURI(),
                    response.getStatus(),
                    duration
            );
        }
    }
}
