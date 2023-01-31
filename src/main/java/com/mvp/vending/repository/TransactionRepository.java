package com.mvp.vending.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvp.vending.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
