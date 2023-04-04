package com.arsuhinars.animals_chipization.exception.handler;

import com.arsuhinars.animals_chipization.exception.AppException;
import jakarta.validation.ValidationException;
import org.springframework.http.*;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({AppException.class})
    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleAppException(AppException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getDetails(), ex.getStatusCode());
    }

    @ExceptionHandler({ValidationException.class})
    @RequestMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> handleValidationException(ValidationException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
    ) {
        var details = ex.getMessage();

        if (body instanceof ProblemDetail) {
            details = ((ProblemDetail)body).getDetail();
        }

        return new ResponseEntity<>(details, statusCode);
    }
}
