package com.training.trackMailApi.exception;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String msg) {
        super(msg);
    }
}
