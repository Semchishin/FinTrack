package com.semchishin.api.exception;

/**
 * Base exception for transaction not found in the application.
 */
public class TransactionNotFoundException extends RuntimeException{

    public TransactionNotFoundException(String message) {
        super(message);
    }

}
