package com.catalog.controller.handler;

import com.catalog.exception.ApplicationException;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;

public class ApplicationExceptionHandler {

    @ExceptionHandler({ApplicationException.class})
    public ResponseEntity<String> handleApplicationException(ApplicationException e) {

        Gson gson = new Gson();
        HashMap<String, String> response = new HashMap<>();
        HttpStatus httpStatus = HttpStatus.valueOf(e.getErrorCode().getHttpStatusCode());

        response.put("message", e.getMessage());
        response.put("code", e.getErrorCode().getCode());

        return ResponseEntity.status(httpStatus).body(gson.toJson(response));
    }
}