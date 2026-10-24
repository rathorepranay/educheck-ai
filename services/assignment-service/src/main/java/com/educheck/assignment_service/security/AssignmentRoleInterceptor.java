package com.educheck.assignment_service.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
public class AssignmentRoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String role = (String) request.getAttribute("authenticatedRole");
        String httpMethod = request.getMethod();

        boolean allowed = switch (httpMethod) {
            case "GET" -> hasRole(role, "STUDENT", "TEACHER", "ADMIN");
            case "POST", "PUT" -> hasRole(role, "TEACHER", "ADMIN");
            case "DELETE" -> hasRole(role, "ADMIN");
            default -> false;
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