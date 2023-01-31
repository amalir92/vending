package com.mvp.vending.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import com.mvp.vending.model.Product;
import com.mvp.vending.repository.ProductRepository;
import com.mvp.vending.service.VendingService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/vending/product")
@Api(tags = "Products")
public class ProductController {

  @Autowired
  private VendingService vendingService;

  private final ProductRepository repository;

  ProductController(ProductRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/all")
  List<Product> all() {
    return repository.findAll();
  }

  @PostMapping("/add")
  @ResponseStatus(HttpStatus.CREATED)
  void newProduct(@RequestBody ArrayList<Product> products) throws Exception {
    for (Product product : products) {
      vendingService.addOrUpdateProducts(product.getProductId().getProductId(), product.getProductId().getSellerId(),
          product.getProductName(), product.getAmountAvailable(), product.getProductCost());
    }
  }

  @DeleteMapping("/remove/{productId}/{sellerId}")
  @ResponseStatus(HttpStatus.OK)
  void removeProduct(@PathVariable String productId, @PathVariable String sellerId) throws Exception {
      vendingService.removeProducts(productId, sellerId);
  }

  @PutMapping("/update/{productId}/{sellerId}")
  @ResponseStatus(HttpStatus.CREATED)
  void updateProduct(@RequestBody Product product, @PathVariable String productId, @PathVariable String sellerId)
      throws Exception {
    vendingService.addOrUpdateProducts(productId, sellerId, product.getProductName(), product.getAmountAvailable(),
        product.getProductCost());
  }
}
