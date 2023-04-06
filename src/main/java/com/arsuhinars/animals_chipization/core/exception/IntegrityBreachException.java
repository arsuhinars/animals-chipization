package com.arsuhinars.animals_chipization.core.exception;

import org.springframework.http.HttpStatus;

public class IntegrityBreachException extends AppException {
    public IntegrityBreachException() {
        super("Data integrity breach error", HttpStatus.BAD_REQUEST);
    }

    public IntegrityBreachException(String details) {
        super(details, HttpStatus.BAD_REQUEST);
    }

    public IntegrityBreachException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
