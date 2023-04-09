package com.arsuhinars.animals_chipization.core.exception;

import org.springframework.http.HttpStatus;

public class InvalidFormatException extends AppException {
    public InvalidFormatException() {
        super("Invalid format", HttpStatus.BAD_REQUEST);
    }

    public InvalidFormatException(String details) {
        super(details, HttpStatus.BAD_REQUEST);
    }

    public InvalidFormatException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
