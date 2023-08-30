package com.training.trackMailApi.exception;

public class UserIsPresentException extends RuntimeException {

    public UserIsPresentException(String msg) {
        super(msg);
    }
}
