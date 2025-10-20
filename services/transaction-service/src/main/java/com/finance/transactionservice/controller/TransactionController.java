package com.finance.transactionservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.dto.ApiResponse;
import com.finance.transactionservice.dto.CreateTransactionRequest;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.finance.logging.LoggingConfig;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Transactions", description = "Endpoints for retrieving and creating transactions")
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponse>> createTransaction(
        @Valid @RequestBody CreateTransactionRequest transactionRequest,
        HttpServletRequest request
    ) {
        LoggingConfig.startRequest(request.getRequestURI(), "transaction-service");
        log.info("Received new transaction request: {}", transactionRequest);

        try {
            TransactionResponse createdTransaction = transactionService.create(transactionRequest);

            ApiResponse<TransactionResponse> response = ApiResponse.<TransactionResponse>builder()
                .success(true)
                .message("Transaction created successfully")
                .data(createdTransaction)
                .build();

            log.info("Completed transaction request: {}", createdTransaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            LoggingConfig.endRequest();
        }
    }
}
