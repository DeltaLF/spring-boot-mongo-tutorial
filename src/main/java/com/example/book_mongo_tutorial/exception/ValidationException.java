package com.example.book_mongo_tutorial.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // Returns a 400 status
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}