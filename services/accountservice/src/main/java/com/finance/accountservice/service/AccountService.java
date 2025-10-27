
package com.finance.accountservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finance.accountservice.model.Account;
import com.finance.accountservice.repository.AccountRepository;
import com.finance.common.dto.TransferEventDTO;

import jakarta.transaction.Transactional;

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

    @Transactional
    public Account deposit(Long accountId, Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        Account account = accountRepository.findById(accountId).orElseThrow();
        account.credit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public boolean transferMoney(TransferEventDTO transfer) {
        try {
            Account sender = accountRepository.findById(transfer.fromAccountId()).orElseThrow();
            Account receiver = accountRepository.findById(transfer.toAccountId()).orElseThrow();
            if (sender.getBalance() >= transfer.amount()) {
                sender.setBalance(sender.getBalance() - transfer.amount());
                receiver.setBalance(receiver.getBalance() + transfer.amount());
                return true;
            } else
                throw new Exception();
        } catch (Exception e) {
            return false;
        }
    }
}
