package com.educheck.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDto(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name should not be more than 100 characters")
        String fullName,

        @NotBlank(message = "Email is required")
        @Email(message = "Enter a valid email address ")
        @Size(max = 254, message = "Email cannot exceed 254 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 12, max = 50, message = "Password must be between 12 to 50 characters")
        String password,

        @NotBlank(message = "Role is required")
        String role
) {

}
