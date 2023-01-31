package com.mvp.vending.model;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {
    @EmbeddedId
    private ProductId productId;
    private String productName;
    private double productCost;
    private int amountAvailable;
}
