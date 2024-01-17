package com.example.cbu.exception.customException;

public class InvalidValidationException extends RuntimeException {

    public InvalidValidationException(String message) {
        super(message);
    }
}
