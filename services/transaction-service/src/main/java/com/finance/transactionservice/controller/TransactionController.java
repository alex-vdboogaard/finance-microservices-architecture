package com.finance.transactionservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.dto.ApiResponse;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.service.TransactionService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.finance.logging.LoggingConfig;
import com.finance.transactionservice.model.Transaction;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/transaction")
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
    public ResponseEntity<ApiResponse<Transaction>> createTransaction(@RequestBody Transaction transaction, HttpServletRequest request) {
        LoggingConfig.startRequest(request.getRequestURI(), "transaction-service");
        log.info("Received new transaction request: {}", transaction.toString());
        Transaction createdTransaction = transactionService.create(transaction);

        ApiResponse<Transaction> response = ApiResponse.<Transaction>builder()
                .success(true)
                .message("Transaction created successfully")
                .data(createdTransaction)
                .build();

        log.info("Completed transaction request: {}", createdTransaction.toString());
        
        LoggingConfig.endRequest();
        return ResponseEntity.ok(response);
    }
    
    
}
