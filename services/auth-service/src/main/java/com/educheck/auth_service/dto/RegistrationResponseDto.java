package com.educheck.auth_service.dto;


import com.educheck.auth_service.entity.Role;

import java.time.Instant;

public record RegistrationResponseDto(
        Long id,
        String fullName,
        String email,
        Role role,
        Instant createdAt

) {
}
