/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.finance.transactionservice.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.finance.transactionservice.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Override
    @EntityGraph(attributePaths = "paymentMethod")
    @NonNull
    List<Transaction> findAll();

    @EntityGraph(attributePaths = "paymentMethod")
    @NonNull
    Page<Transaction> findAll(@NonNull Pageable pageable);
}
