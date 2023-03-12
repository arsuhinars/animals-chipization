package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class DependsOnException extends AppException {
    public DependsOnException() {
        super("Entity depends on another one", HttpStatus.BAD_REQUEST);
    }

    public DependsOnException(String details) {
        super(details, HttpStatus.BAD_REQUEST);
    }

    public DependsOnException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
