package com.finance.accountservice.dto;

import jakarta.validation.constraints.NotNull;

public record CreateAccountRequest(@NotNull Long userId, @NotNull String accountNumber) {

}
