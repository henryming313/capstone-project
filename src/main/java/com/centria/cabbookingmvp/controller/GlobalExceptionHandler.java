package com.centria.cabbookingmvp.controller;

import com.centria.cabbookingmvp.controller.dto.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<?> handleRuntime(RuntimeException e) {
        return ApiResponse.error(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<?> handleValidation(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getFieldError() != null
                ? e.getBindingResult().getFieldError().getDefaultMessage()
                : "Validation failed";
        return ApiResponse.error(msg);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ApiResponse<?> handleBadJson(HttpMessageNotReadableException e) {
        return ApiResponse.error("Invalid request body");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ApiResponse<?> handleDatabase(DataIntegrityViolationException e) {
        return ApiResponse.error("Database constraint error");
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<?> handleOther(Exception e) {
        return ApiResponse.error("Server error: " + e.getClass().getSimpleName());
    }
}