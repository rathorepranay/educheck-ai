package com.educheck.auth_service.dto;

import com.educheck.auth_service.entity.Role;

public record LoginResponseDto(
        String accessToken,
        String tokenType,
        long expiresIn,
        Long userId,
        String email,
        String userEmail, Role role
) {
}