package com.mvp.vending.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long transactionId;
    private ProductId productId;
    private int amount;

    public Transaction(ProductId productId, int amount) {
        this.productId = productId;
        this.amount = amount;
    }

}
