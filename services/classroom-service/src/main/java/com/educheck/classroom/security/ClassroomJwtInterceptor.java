package com.educheck.classroom.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class ClassroomJwtInterceptor implements HandlerInterceptor {

    private final ClassroomJwtService jwtService;

    public ClassroomJwtInterceptor(ClassroomJwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            writeUnauthorized(response, "Missing Bearer token");
            return false;
        }

        try {
            String token = header.substring(7);
            Claims claims = jwtService.validateToken(token);

            request.setAttribute("authenticatedEmail", claims.getSubject());
            request.setAttribute(
                    "authenticatedRole",
                    claims.get("role", String.class)
            );

            return true;

        } catch (JwtException | IllegalArgumentException ex) {
            writeUnauthorized(response, "Invalid or expired JWT token");
            return false;
        }
    }

    private void writeUnauthorized(
            HttpServletResponse response,
            String message) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(
                "{\"message\":\"" + message + "\"}"
        );
    }
}