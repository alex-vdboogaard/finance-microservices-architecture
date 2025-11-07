
package com.finance.accountservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.dto.DepositRequest;
import com.finance.accountservice.model.Account;
import com.finance.accountservice.service.AccountService;
import com.finance.common.dto.ApiResponse;
import com.finance.common.dto.PageResponse;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Accounts", description = "Endpoints for retrieving and creating accounts")
@RequestMapping("/api/v1/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<Account>>> getAccounts(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Account> page = accountService.getAccounts(pageable);

        PageResponse<Account> data = PageResponse.<Account>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        ApiResponse<PageResponse<Account>> response = ApiResponse
                .<PageResponse<Account>>builder()
                .meta(ApiResponse.Meta.builder().message("Accounts fetched successfully").build())
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Account>> createAccount(@RequestBody @Valid CreateAccountRequest request) {
        Account created = accountService.create(request);

        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .meta(ApiResponse.Meta.builder().message("Account created successfully").build())
                .data(created)
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<Account>> deposit(
            @PathVariable("id") Long accountId,
            @RequestBody @Valid DepositRequest request) {
        Account updated = accountService.deposit(accountId, request.amount());

        ApiResponse<Account> response = ApiResponse.<Account>builder()
                .data(updated)
                .meta(ApiResponse.Meta.builder()
                        .message("Deposit successful")
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

}
