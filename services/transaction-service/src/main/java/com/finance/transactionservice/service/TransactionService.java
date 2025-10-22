/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.finance.transactionservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.finance.common.model.CompletedTransaction;
import com.finance.transactionservice.dto.CreateTransactionRequest;
import com.finance.transactionservice.dto.TransactionResponse;
import com.finance.transactionservice.mapper.TransactionMapper;
import com.finance.transactionservice.model.PaymentMethod;
import com.finance.transactionservice.model.Transaction;
import com.finance.transactionservice.model.Transaction.TransactionStatus;
import com.finance.transactionservice.repository.PaymentMethodRepository;
import com.finance.transactionservice.repository.TransactionRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final TransactionProducer producer;

    public TransactionService(
            TransactionRepository transactionRepository,
            PaymentMethodRepository paymentMethodRepository,
            TransactionProducer producer) {
        this.transactionRepository = transactionRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.producer = producer;
    }

    public List<TransactionResponse> findAll() {
        return transactionRepository.findAll()
                .stream()
                .map(TransactionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public TransactionResponse create(CreateTransactionRequest request) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(request.paymentMethodId())
                .orElseThrow(
                        () -> new NotFoundException("Payment method not found with id " + request.paymentMethodId()));

        Transaction transaction = Transaction.builder()
                .amount(request.amount())
                .UserId(request.UserId())
                .paymentMethod(paymentMethod)
                .status(resolveStatus(request.status()))
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        CompletedTransaction send = TransactionMapper.toCompletedTransaction(savedTransaction);

        producer.sendTransaction(send);

        TransactionResponse response = TransactionMapper.toResponse(savedTransaction);

        return response;
    }

    public Transaction update(Long id, Transaction updatedTransaction) {
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found with id " + id));
        existing.setAmount(updatedTransaction.getAmount());
        return transactionRepository.save(existing);
    }

    public void delete(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException("Transaction not found with id " + id);
        }
        transactionRepository.deleteById(id);
    }

    private TransactionStatus resolveStatus(String status) {
        if (status == null || status.isBlank()) {
            return TransactionStatus.PENDING;
        }

        try {
            return TransactionStatus.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid transaction status: " + status);
        }
    }
}
