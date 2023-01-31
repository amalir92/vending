package com.mvp.vending.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvp.vending.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String username);

}
