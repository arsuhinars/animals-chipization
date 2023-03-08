package com.arsuhinars.animals_chipization.exception;

import org.springframework.http.HttpStatus;

public class LastItemException extends AppException {
    public LastItemException() {
        super("Entity is last item in the list", HttpStatus.BAD_REQUEST);
    }

    public LastItemException(String details) {
        super(details, HttpStatus.BAD_REQUEST);
    }

    public LastItemException(String details, HttpStatus statusCode) {
        super(details, statusCode);
    }
}
