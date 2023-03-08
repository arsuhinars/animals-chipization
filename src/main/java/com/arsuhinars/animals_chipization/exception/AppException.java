package com.arsuhinars.animals_chipization.exception;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class AppException extends RuntimeException {
    @Setter(AccessLevel.PRIVATE)
    private String details;

    @Setter(AccessLevel.PRIVATE)
    private HttpStatus statusCode;

    public AppException(String details, HttpStatus statusCode) {
        super(details);

        setDetails(details);
        setStatusCode(statusCode);
    }
}
