package com.arsuhinars.animals_chipization.core.exception;

import org.springframework.http.HttpStatus;

public class AlreadyExistException extends AppException {
    public AlreadyExistException() {
        super("Same entity already exists", HttpStatus.CONFLICT);
    }

    public AlreadyExistException(String details) {
        super(details, HttpStatus.CONFLICT);
    }

    public AlreadyExistException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
