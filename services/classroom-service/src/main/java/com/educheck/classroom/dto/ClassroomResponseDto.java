package com.educheck.classroom.dto;

import java.time.Instant;

public record ClassroomResponseDto(
        Long id,
        String name,
        String description,
        Instant createdAt
) {
}
