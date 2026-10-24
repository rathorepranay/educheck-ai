package com.educheck.classroom.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;

@Component
public class ClassroomRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler) throws IOException {

        if (!(handler instanceof HandlerMethod method)) {
            return true;
        }

        String role = (String) request.getAttribute("authenticatedRole");
        String httpMethod = request.getMethod();
        String path = request.getRequestURI();

        boolean allowed = switch (httpMethod) {
            case "GET" -> hasRole(role, "STUDENT", "TEACHER", "ADMIN");
            case "POST" -> hasRole(role, "TEACHER", "ADMIN");
            case "PUT" -> hasRole(role, "TEACHER", "ADMIN");
            case "DELETE" -> hasRole(role, "ADMIN");
            case null, default -> false;
        };

        if (!allowed) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write(
                    "{\"message\":\"Your role is not permitted to perform this operation\"}"
            );
            return false;
        }

        return true;
    }

    private boolean hasRole(String actualRole, String... allowedRoles) {
        if (actualRole == null) {
            return false;
        }

        for (String allowedRole : allowedRoles) {
            if (allowedRole.equals(actualRole)) {
                return true;
            }
        }

        return false;
    }
}