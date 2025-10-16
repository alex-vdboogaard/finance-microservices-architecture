/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.finance.transactionservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finance.transactionservice.model.Transaction;
import com.finance.transactionservice.repository.TransactionRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class TransactionService {
    @Autowired
    public TransactionRepository transactionRepository;

    public List<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    public Transaction create(Transaction transaction) {
        return transactionRepository.save(transaction);
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
}
