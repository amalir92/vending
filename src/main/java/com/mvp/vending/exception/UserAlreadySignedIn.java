package com.mvp.vending.exception;

public class UserAlreadySignedIn extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadySignedIn() {
        super("There is already an active session using your account!");
    }

}