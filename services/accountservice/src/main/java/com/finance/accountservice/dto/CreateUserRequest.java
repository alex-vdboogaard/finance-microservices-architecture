// @formatter:off

package com.finance.accountservice.dto;

import jakarta.validation.constraints.*;

public record CreateUserRequest(
    @NotNull
    @Size(min = 2, message = "First name must be at least 2 characters")
    @Size(max = 30, message = "First name can be max 30 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters are allowed")
    String firstName,

    @NotNull
    @Size(min = 2, message = "Last name must be at least 2 characters")
    @Size(max = 50, message = "Last name can be max 50 characters")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Only letters are allowed")
    String lastName,

    @NotNull
    @Size(min = 10, max = 10, message = "Government ID must be 10 digits long")
    @Pattern(regexp = "\\d+", message = "Government ID must contain only digits")
    String governmentId,

    @NotNull
    @Email(message = "Invalid email format")
    String email,

    @NotNull
    @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    @Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain upper, lower, digit, and special character"
    )
    String password
) {}

// @formatter:on
