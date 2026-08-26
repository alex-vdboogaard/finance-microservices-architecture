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
import com.finance.common.logging.LoggingConfig;
import com.finance.transactionservice.dto.TransactionResponseDTO;
import com.finance.transactionservice.dto.TransferRequestDTO;
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
    public ResponseEntity<ApiResponse<PageResponse<TransactionResponseDTO>>> getAllTransactions(
            @PageableDefault(size = 10) Pageable pageable) {
        Page<TransactionResponseDTO> page = transactionService.findAll(pageable);

        PageResponse<TransactionResponseDTO> data = PageResponse.<TransactionResponseDTO>builder()
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

        ApiResponse<PageResponse<TransactionResponseDTO>> response = ApiResponse
                .<PageResponse<TransactionResponseDTO>>builder()
                .meta(ApiResponse.Meta.builder().message("Transactions fetched successfully").build())
                .data(data)
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TransactionResponseDTO>> createTransaction(
            @Valid @RequestBody TransferRequestDTO transactionRequest,
            HttpServletRequest request) {
        LoggingConfig.startRequest(request.getRequestURI(), "transaction-service");
        log.info("Received new transaction request: {}", transactionRequest);

        try {
            TransactionResponseDTO createdTransaction = transactionService.create(transactionRequest);

            ApiResponse<TransactionResponseDTO> response = ApiResponse.<TransactionResponseDTO>builder()
                    .meta(ApiResponse.Meta.builder().message("Transaction created successfully").build())
                    .data(createdTransaction)
                    .build();

            log.info("Completed transaction request: {}", createdTransaction);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } finally {
            LoggingConfig.endRequest();
        }
    }
}
