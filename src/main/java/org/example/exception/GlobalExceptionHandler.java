package org.example.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException ex) {

        return ResponseEntity.status(ex.getStatus()).body(
                Map.of(
                        "timestamp", LocalDateTime.now(),
                        "status", ex.getStatus(),
                        "message", ex.getMessage()
                )
        );
    }
}