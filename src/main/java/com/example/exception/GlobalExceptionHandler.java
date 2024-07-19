package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(GitHubServiceException.class)
    public ResponseEntity<ErrorResponse> handleUserException(GitHubServiceException ex) {
        log.error("User exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getReason(),
                String.valueOf(ex.getStatusCode().value())
        );
        return new ResponseEntity<>(errorResponse, ex.getStatusCode());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("An unexpected error occurred: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
