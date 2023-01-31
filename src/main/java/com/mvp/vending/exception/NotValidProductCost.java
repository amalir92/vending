package com.mvp.vending.exception;

public class NotValidProductCost extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotValidProductCost() {
        super("Product cost should be in multiples of 5");
    }

}
