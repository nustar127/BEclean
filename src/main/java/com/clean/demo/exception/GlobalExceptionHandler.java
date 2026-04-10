package com.clean.demo.exception; // Укажите ваш пакет

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleBadCredentials(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Unauthorized");

        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        body.put("message", errorMessage);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handlehzCredentials(RuntimeException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Unauthorized");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        System.out.println("!!! ПОЙМАЛ ОШИБКУ !!! " + ex.getClass().getName());
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.UNAUTHORIZED.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
