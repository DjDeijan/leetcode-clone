package com.leetcode.clone.leetcode_clone.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
        @Pattern(regexp = ".*\\d.*", message = "Password must contain at least one number")
        String password,

        @NotBlank(message = "User first name cannot be null or empty")
        @Size(min = 2, max = 20, message = "User first name must be between 2 and 20 characters")
        String username

) {
    public RegisterRequestDTO {
        email = safeTrim(email);
        // Don't trim password
        username = safeTrim(username);
    }

    private String safeTrim(String str) {
        return str != null ? str.trim() : null;
    }

}
