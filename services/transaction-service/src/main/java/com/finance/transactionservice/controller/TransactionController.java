package com.finance.transactionservice.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finance.common.dto.ApiResponse;
import com.finance.common.dto.PageResponse;
import com.finance.common.dto.TransferEventDTO;
import com.finance.common.dto.TransferRequestDTO;
import com.finance.common.logging.LoggingConfig;
import com.finance.transactionservice.service.TransactionService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Tag(name = "Transactions", description = "Endpoints for retrieving and creating transactions")
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TransferEventDTO>>> getAllTransactions(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TransferEventDTO> page = transactionService.findAll(pageable);

        PageResponse<TransferEventDTO> data = PageResponse.<TransferEventDTO>builder()
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

        ApiResponse<PageResponse<TransferEventDTO>> response = ApiResponse
                .<PageResponse<TransferEventDTO>>builder()
                .success(true)
                .message("Transactions fetched successfully")
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransferEventDTO>> createTransaction(
            @Valid @RequestBody TransferRequestDTO transactionRequest,
            HttpServletRequest request) {
        LoggingConfig.startRequest(request.getRequestURI(), "transaction-service");
        log.info("Received new transaction request: {}", transactionRequest);

        try {
            TransferEventDTO createdTransaction = transactionService.create(transactionRequest);

            ApiResponse<TransferEventDTO> response = ApiResponse.<TransferEventDTO>builder()
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
