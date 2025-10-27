/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.finance.transactionservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.finance.common.dto.TransferEventDTO;
import com.finance.common.dto.TransferRequestDTO;
import com.finance.transactionservice.model.Transaction;
import com.finance.transactionservice.model.Transaction.TransactionStatus;
import com.finance.transactionservice.repository.TransactionRepository;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionProducer producer;

    public TransactionService(
            TransactionRepository transactionRepository,
            TransactionProducer producer) {
        this.transactionRepository = transactionRepository;
        this.producer = producer;
    }

    @Transactional(readOnly = true)
    public Page<TransferEventDTO> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(tx -> new TransferEventDTO(
                        tx.getTransactionId(),
                        tx.getFromAccountId(),
                        tx.getToAccountId(),
                        tx.getAmount(),
                        tx.getStatus().name(),
                        tx.getUpdatedAt()));
    }

    @Transactional
    public TransferEventDTO create(TransferRequestDTO request) {
        // Build initial transaction record
        Transaction transaction = Transaction.builder()
                .transactionId(java.util.UUID.randomUUID().toString())
                .fromAccountId(request.fromAccountId())
                .toAccountId(request.toAccountId())
                .amount(request.amount())
                .status(TransactionStatus.PENDING)
                .build();

        // Save to database
        transactionRepository.save(transaction);

        // Convert to TransferEventDTO for Kafka
        TransferEventDTO event = new TransferEventDTO(
                transaction.getTransactionId(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmount(),
                transaction.getStatus().name(),
                transaction.getCreatedAt());

        // Publish to Kafka
        producer.sendTransactionInitiated(event);

        return event;
    }

}
