package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends AppException {
    public NotFoundException() {
        super("Entity was not found", HttpStatus.NOT_FOUND.value());
    }

    public NotFoundException(String details) {
        super(details, HttpStatus.NOT_FOUND.value());
    }

    public NotFoundException(String details, Integer statusCode) {
        super(details, statusCode);
    }
}
