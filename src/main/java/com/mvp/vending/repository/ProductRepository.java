package com.mvp.vending.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mvp.vending.model.Product;
import com.mvp.vending.model.ProductId;

public interface ProductRepository extends JpaRepository<Product, ProductId> {

}
