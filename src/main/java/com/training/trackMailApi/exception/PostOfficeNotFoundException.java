package com.training.trackMailApi.exception;

public class PostOfficeNotFoundException extends RuntimeException {

    public PostOfficeNotFoundException(String message) {
        super(message);
    }
}
