package com.v1.bms.project.api.exception;

import com.v1.bms.project.domain.vo.exceptions.CustomerAccountNotFoundException;
import com.v1.bms.project.domain.vo.exceptions.CustomerCpfAlreadyInUse;
import com.v1.bms.project.domain.vo.exceptions.UnderageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerCpfAlreadyInUse.class)
    public ResponseEntity<Map<String, String>> handleCustomerCpfAlreadyInUse(
            CustomerCpfAlreadyInUse exception) {

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(CustomerAccountNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCustomerAccountNotFound(
            CustomerAccountNotFoundException exception) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(UnderageException.class)
    public ResponseEntity<Map<String, String>> handleUnderage(
            UnderageException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", exception.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(
            IllegalArgumentException exception) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("message", exception.getMessage()));
    }
}
