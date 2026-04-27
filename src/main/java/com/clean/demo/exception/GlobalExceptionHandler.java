package com.clean.demo.exception; // Укажите ваш пакет

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.clean.demo.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadCredentials(MethodArgumentNotValidException ex) {
        String firstErrorMessage = ex.getBindingResult()
                .getAllErrors()
                .get(0)
                .getDefaultMessage();

        ApiResponse<Object> body = ApiResponse.builder()
                .ok(false) 
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(firstErrorMessage)
                .message("Validation failed")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handlehzCredentials(RuntimeException ex) {
        ApiResponse<Object> body = ApiResponse.builder()
                .ok(false)
                .status(HttpStatus.BAD_REQUEST.value())
                .error(ex.getMessage())
                .message("error")
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {
        ApiResponse<Object> body = ApiResponse.builder()
                .ok(false)
                .status(HttpStatus.UNAUTHORIZED.value())
                .error(ex.getMessage())
                .message("error")
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }
}
