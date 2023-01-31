package com.mvp.vending.exception;

public class NotEnoughDeposit extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotEnoughDeposit() {
        super("Not enough deposit to proceed transaction");
    }

}