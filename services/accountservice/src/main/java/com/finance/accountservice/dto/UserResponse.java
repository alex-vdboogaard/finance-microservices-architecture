package com.finance.accountservice.dto;

public record UserResponse(
        Long userId, String firstName, String lastName, String email, String governmentId) {

}
