package com.mvp.vending.exception;

public class InvalidProductException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidProductException() {
        super("Invalid Product entered!");
    }
}