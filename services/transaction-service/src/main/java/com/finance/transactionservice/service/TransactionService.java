/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.finance.transactionservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finance.common.dto.TransferEventDTO;
import com.finance.transactionservice.dto.TransferRequestDTO;
import com.finance.transactionservice.mapper.TransactionMapper;
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
    @Cacheable(value = "transactions", key = "#pageable.pageNumber + '-' + #pageable.pageSize + '-' + #pageable.sort.toString()")
    public Page<TransferEventDTO> findAll(Pageable pageable) {
        return transactionRepository.findAll(pageable)
                .map(tx -> new TransferEventDTO(
                        tx.getTransactionId(),
                        tx.getFromAccountId(),
                        tx.getToAccountId(),
                        tx.getAmount(),
                        tx.getStatus().name(),
                        tx.getDescription(),
                        tx.getUpdatedAt()));
    }

    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
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
        Transaction saved = transactionRepository.save(transaction);

        // Convert to TransferEventDTO for Kafka
        TransferEventDTO event = new TransferEventDTO(
                saved.getTransactionId(),
                saved.getFromAccountId(),
                saved.getToAccountId(),
                saved.getAmount(),
                saved.getStatus().name(),
                saved.getCreatedAt());

        // Publish to Kafka
        producer.sendTransactionInitiated(event);

        return event;
    }

    @Transactional
    @CacheEvict(value = "transactions", allEntries = true)
    public TransferEventDTO updateStatusAndDescription(TransferEventDTO event, Transaction.TransactionStatus status) {
        Transaction tx = transactionRepository.findById(event.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        tx.setStatus(status);
        tx.setDescription(event.description() != null ? event.description() : null);
        return TransactionMapper.toDTO(tx);
    }
}
