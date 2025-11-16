
package com.finance.accountservice.controller;

import java.util.List;

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

import com.finance.accountservice.dto.AccountResponse;
import com.finance.accountservice.dto.CreateAccountRequest;
import com.finance.accountservice.dto.DepositRequest;
import com.finance.accountservice.mapper.AccountMapper;
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
    public ResponseEntity<ApiResponse<PageResponse<AccountResponse>>> getAccounts(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<Account> page = accountService.getAccounts(pageable);

        List<AccountResponse> content = AccountMapper.toResponseList(page.getContent());

        PageResponse<AccountResponse> data = PageResponse.<AccountResponse>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        ApiResponse<PageResponse<AccountResponse>> response = ApiResponse
                .<PageResponse<AccountResponse>>builder()
                .meta(ApiResponse.Meta.builder().message("Accounts fetched successfully").build())
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @RequestBody @Valid CreateAccountRequest request) {
        Account created = accountService.create(request);

        AccountResponse accountResponse = AccountMapper.toResponse(created);

        ApiResponse<AccountResponse> response = ApiResponse.<AccountResponse>builder()
                .meta(ApiResponse.Meta.builder()
                        .message("Account created successfully")
                        .build())
                .data(accountResponse)
                .build();

        return ResponseEntity.status(201).body(response);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<ApiResponse<AccountResponse>> deposit(
            @PathVariable("id") Long accountId,
            @RequestBody @Valid DepositRequest request) {
        Account updated = accountService.deposit(accountId, request.amount());

        AccountResponse accountResponse = AccountMapper.toResponse(updated);

        ApiResponse<AccountResponse> response = ApiResponse.<AccountResponse>builder()
                .data(accountResponse)
                .meta(ApiResponse.Meta.builder()
                        .message("Deposit successful")
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }

}
