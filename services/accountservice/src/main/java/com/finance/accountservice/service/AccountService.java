
package com.finance.accountservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finance.accountservice.model.Account;
import com.finance.accountservice.model.User;
import com.finance.accountservice.repository.AccountRepository;
import com.finance.accountservice.repository.UserRepository;
import com.finance.common.dto.TransferEventDTO;

import jakarta.transaction.Transactional;

import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.exception.UserNotFoundException;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Page<Account> getAccounts(Pageable pageable) {
        return accountRepository.findAll(pageable);
    }

    public Account create(CreateAccountRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Account account = Account.builder()
                .user(user)
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
