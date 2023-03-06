package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class BoundException extends AppException {
    public BoundException() {
        super("Entity is bounded", HttpStatus.BAD_REQUEST.value());
    }

    public BoundException(String details) {
        super(details, HttpStatus.BAD_REQUEST.value());
    }

    public BoundException(String details, Integer statusCode) {
        super(details, statusCode);
    }
}
