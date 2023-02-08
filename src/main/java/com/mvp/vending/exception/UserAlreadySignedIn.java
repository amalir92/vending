package com.mvp.vending.exception;

public class UserAlreadySignedIn extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadySignedIn() {
        super("User user already logged in. Please close other sessions to proceed");
    }

}