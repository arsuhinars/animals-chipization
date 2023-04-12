package com.arsuhinars.animals_chipization.core.exception;

import org.springframework.http.HttpStatus;

public class UnresolvedException extends AppException {
    public UnresolvedException() {
        super("Unresolved error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UnresolvedException(String details) {
        super(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public UnresolvedException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
