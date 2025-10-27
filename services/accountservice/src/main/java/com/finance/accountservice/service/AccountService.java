
package com.finance.accountservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finance.accountservice.model.Account;
import com.finance.accountservice.repository.AccountRepository;
import com.finance.accountservice.dto.CreateAccountRequest;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Page<Account> getAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public Account create(CreateAccountRequest request) {
        Account account = Account.builder()
                .userId(request.userId())
                .accountNumber(request.accountNumber())
                .balance(0.0)
                .build();
        return accountRepository.save(account);
    }
}
