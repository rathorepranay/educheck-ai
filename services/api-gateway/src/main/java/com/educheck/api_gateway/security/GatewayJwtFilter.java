package com.educheck.api_gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class GatewayJwtFilter implements HandlerInterceptor {

    private final GatewayJwtService gatewayJwtService;

    public GatewayJwtFilter(GatewayJwtService gatewayJwtService) {
        this.gatewayJwtService = gatewayJwtService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws IOException {
        System.out.println(
                "JWT interceptor invoked: " + request.getRequestURI()
        );

        String path = request.getRequestURI();

        // Public authentication endpoints
        if (path.equals("/api/auth/login")
                || path.equals("/api/auth/register")
                || path.startsWith("/actuator/health")) {
            return true;
        }

        // Require a Bearer token for all other requests
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Missing Bearer token\"}"
            );
            return false;
        }

        try {
            String token = header.substring(7);
            Claims claims = gatewayJwtService.validateToken(token);

            request.setAttribute("authenticatedEmail", claims.getSubject());
            request.setAttribute("authenticatedRole",
                    claims.get("role", String.class));

            return true;

        } catch (JwtException | IllegalArgumentException ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Invalid or expired JWT token\"}"
            );
            return false;
        }
    }
}