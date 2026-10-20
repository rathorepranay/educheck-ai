package com.educheck.assignment_service.dto;

import java.time.Instant;
import java.time.LocalDate;

public record AssignmentResponseDto(
        Long id,
        long classroomId,
        String title,
        String description,
        LocalDate dueDate,
        Instant createdAt
) {
}
