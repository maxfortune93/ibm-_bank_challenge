package com.ibm_bank_challenge.exception;

/** Business rule violation that maps to HTTP 400. */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
