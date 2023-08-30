package com.training.trackMailApi.exception;

public class InvalidMailingStatusException extends RuntimeException {

    public InvalidMailingStatusException(String message) {
        super(message);
    }
}
