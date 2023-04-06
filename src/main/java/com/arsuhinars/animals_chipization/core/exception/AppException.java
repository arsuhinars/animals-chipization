package com.arsuhinars.animals_chipization.core.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
public class AppException extends RuntimeException {
    private final String details;

    private final HttpStatus statusCode;

    public AppException(String details, HttpStatus statusCode) {
        super(details);

        this.details = details;
        this.statusCode = statusCode;
    }
}
