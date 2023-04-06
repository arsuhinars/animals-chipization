package com.arsuhinars.animals_chipization.core.exception;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends AppException {
    public ForbiddenException() {
        super("Access is denied", HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String details) {
        super(details, HttpStatus.FORBIDDEN);
    }

    public ForbiddenException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
