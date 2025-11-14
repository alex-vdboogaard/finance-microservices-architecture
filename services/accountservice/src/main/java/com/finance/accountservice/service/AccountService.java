
package com.finance.accountservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.exception.AccountNotFoundException;
import com.finance.accountservice.exception.InvalidAmountException;
import com.finance.accountservice.exception.UserNotFoundException;
import com.finance.accountservice.model.Account;
import com.finance.accountservice.model.User;
import com.finance.accountservice.repository.AccountRepository;
import com.finance.accountservice.repository.UserRepository;
import com.finance.common.dto.TransferEventDTO;

import jakarta.transaction.Transactional;

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
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account with ID " + accountId + " not found"));
        account.credit(amount);
        return accountRepository.save(account);
    }

    @Transactional
    public TransferEventDTO transferMoney(TransferEventDTO transfer) {
        String status;
        String description;

        try {
            if (transfer.amount() == null || transfer.amount() <= 0) {
                throw new IllegalArgumentException("Transfer amount must be greater than zero");
            }

            Account sender = accountRepository.findById(transfer.fromAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
            Account receiver = accountRepository.findById(transfer.toAccountId())
                    .orElseThrow(() -> new IllegalArgumentException("Receiver account not found"));

            if (sender.getBalance() < transfer.amount()) {
                throw new IllegalStateException("Insufficient funds");
            }

            sender.debit(transfer.amount());
            receiver.credit(transfer.amount());

            status = "SUCCESS";
            description = "Remaining balance: " + sender.getBalance();
        } catch (IllegalArgumentException | IllegalStateException e) {
            status = "FAILED";
            description = e.getMessage() != null ? e.getMessage() : "Transfer failed";
        }

        return new TransferEventDTO(
                transfer.transactionId(),
                transfer.fromAccountId(),
                transfer.toAccountId(),
                transfer.amount(),
                status,
                description,
                transfer.timestamp());
    }
}
