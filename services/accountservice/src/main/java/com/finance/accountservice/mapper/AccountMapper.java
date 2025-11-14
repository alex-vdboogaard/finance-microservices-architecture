package com.finance.accountservice.mapper;

import java.util.List;

import com.finance.accountservice.dto.AccountResponse;
import com.finance.accountservice.model.Account;

public final class AccountMapper {
    private AccountMapper() {
    }

    public static AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreatedAt(),
                account.getUpdatedAt());
    }

    public static List<AccountResponse> toResponseList(List<Account> accounts) {
        if (accounts == null) {
            return List.of();
        }
        return accounts.stream()
                .map(AccountMapper::toResponse)
                .toList();
    }
}
