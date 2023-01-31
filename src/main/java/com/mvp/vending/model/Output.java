package com.mvp.vending.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Output implements Serializable {

    private String productId;
    private int amount;
    private double totalCost;
    private int[] remainingCash;

}
