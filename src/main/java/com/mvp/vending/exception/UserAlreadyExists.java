package com.mvp.vending.exception;

public class UserAlreadyExists extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExists() {
        super("User already exists. Please signin");
    }

}