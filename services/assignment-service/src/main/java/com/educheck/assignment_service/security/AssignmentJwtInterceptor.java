package com.educheck.assignment_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AssignmentJwtInterceptor implements HandlerInterceptor {

    private final AssignmentJwtService jwtService;

    public AssignmentJwtInterceptor(AssignmentJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            return unauthorized(response, "Missing Bearer token");
        }

        try {
            Claims claims = jwtService.validateToken(header.substring(7));

            request.setAttribute("authenticatedEmail", claims.getSubject());
            request.setAttribute(
                    "authenticatedRole",
                    claims.get("role", String.class)
            );

            return true;

        } catch (JwtException | IllegalArgumentException ex) {
            return unauthorized(response, "Invalid or expired JWT token");
        }
    }

    private boolean unauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"message\":\"" + message + "\"}"
        );

        return false;
    }
}