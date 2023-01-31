package com.mvp.vending;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mvp.vending.model.Product;
import com.mvp.vending.model.ProductId;
import com.mvp.vending.model.Transaction;
import com.mvp.vending.model.User;
import com.mvp.vending.repository.ProductRepository;
import com.mvp.vending.repository.TransactionRepository;
import com.mvp.vending.repository.UserRepository;

@Configuration
/**
 * @param ProductRepository
 * @param UserRepository
 * @return
 */
class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(ProductRepository ProductRepository, UserRepository userRepository,
      TransactionRepository transactionRepository, PasswordEncoder passwordEncoder) {

    return args -> {
      log.info("PreLoading " + userRepository.save(User.builder()
          .username("user1")
          .password(passwordEncoder.encode("password"))
          .roles(Arrays.asList("BUYER"))
          .build()));

      log.info("PreLoading " + userRepository.save(User.builder()
          .username("admin1")
          .password(passwordEncoder.encode("password"))
          .roles(Arrays.asList("SELLER"))
          .build()));
      log.info(
          "PreLoading " + ProductRepository.save(new Product(new ProductId("001", "s001"), "Coke", 50.00, 100)));
      log.info(
          "PreLoading " + ProductRepository.save(new Product(new ProductId("002", "s001"), "Fanta", 30.00, 100)));
      log.info("PreLoading " + transactionRepository.save(new Transaction(new ProductId("001", "seller1"), 1)));

    };
  }

}
