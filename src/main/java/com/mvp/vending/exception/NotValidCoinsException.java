package com.mvp.vending.exception;

public class NotValidCoinsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NotValidCoinsException() {
        super("Please deposit valid coins of 5, 10, 20, 50 ,100");
    }

}
