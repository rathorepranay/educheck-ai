package com.educheck.assignment_service.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateAssignmentDto (

    @NotNull(message = "Classroom ID is required")
    Long classroomId,

    @NotBlank(message = "Title required")
    @Size(max = 150, message = "Title should be less than 150 characters")
    String title,

    @Size(max = 500, message = "Description should be less than 500 characters")
    String description,

    @NotNull(message = "Due Date is required")
    @FutureOrPresent(message = "Date must be today or in future")
    LocalDate dueDate)
{}
