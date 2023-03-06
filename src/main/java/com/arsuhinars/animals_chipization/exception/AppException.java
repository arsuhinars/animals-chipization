package com.arsuhinars.animals_chipization.exception;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@EqualsAndHashCode(callSuper = true)
@Data
public class AppException extends Exception {
    @Setter(AccessLevel.PRIVATE)
    private String details;

    @Setter(AccessLevel.PRIVATE)
    private Integer statusCode;

    public AppException(String details, Integer statusCode) {
        super(details);

        setDetails(details);
        setStatusCode(statusCode);
    }
}
