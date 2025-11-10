package com.finance.accountservice.dto;

import java.util.List;

public record UserAndAccounts(
        Long userId,
        String firstName,
        String lastName,
        String email,
        String govermentId,
        List<AccountResponse> accounts) {
}
