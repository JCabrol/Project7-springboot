package com.nnk.springboot.exceptions;

public class PageNotAuthorizedException extends RuntimeException {
    public PageNotAuthorizedException(String message) {
        super(message);
    }
}
