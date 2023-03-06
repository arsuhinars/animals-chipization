package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends AppException {
    public AlreadyExistException() {
        super("Same entity already exists", HttpStatus.CONFLICT.value());
    }

    public AlreadyExistException(String details) {
        super(details, HttpStatus.CONFLICT.value());
    }

    public AlreadyExistException(String details, Integer statusCode) {
        super(details, statusCode);
    }
}
