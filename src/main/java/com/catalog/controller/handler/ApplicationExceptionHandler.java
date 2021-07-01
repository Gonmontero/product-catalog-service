package com.catalog.controller.handler;

import com.catalog.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class ApplicationExceptionHandler {

    @ExceptionHandler({ ApplicationException.class })
    public ResponseEntity<String> handleApplicationException(ApplicationException e) {

        HttpStatus httpStatus = HttpStatus.valueOf(e.getErrorCode().getHttpStatusCode());
        return ResponseEntity.status(httpStatus).body(e.getMessage());
    }
}