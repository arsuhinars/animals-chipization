package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class LastItemException extends AppException {
    public LastItemException() {
        super("Entity is last item in the list", HttpStatus.BAD_REQUEST.value());
    }

    public LastItemException(String details) {
        super(details, HttpStatus.BAD_REQUEST.value());
    }

    public LastItemException(String details, Integer statusCode) {
        super(details, statusCode);
    }
}
