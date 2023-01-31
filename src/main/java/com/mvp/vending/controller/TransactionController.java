package com.mvp.vending.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mvp.vending.model.Transaction;
import com.mvp.vending.repository.TransactionRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/vending/transaction")
@Api(tags = "Transactions")
public class TransactionController {

  private final TransactionRepository repository;

  TransactionController(TransactionRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/all")
  List<Transaction> all() {
    return repository.findAll();
  }

}
