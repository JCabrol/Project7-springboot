package com.nnk.springboot.exceptions;

public class ObjectAlreadyExistingException extends RuntimeException {
    public ObjectAlreadyExistingException(String message) {
        super(message);
    }
}
