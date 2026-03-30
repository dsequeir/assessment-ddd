package com.example.assessmentddd.infrastructure.web;

import com.example.assessmentddd.application.exception.PriceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                "Invalid request parameter: " + ex.getName(),
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(PriceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePriceNotFound(
            PriceNotFoundException ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "PRICE_NOT_FOUND",
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericError(
            Exception ex,
            HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                "Unexpected error",
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );

        log.error("Unexpected error {}", ex.getMessage());

        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleConstraintViolationException(ConstraintViolationException ex,
                                                                               HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleMissingParamsException(MissingServletRequestParameterException ex,
                                                                         HttpServletRequest request) {

        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(error.getStatus()).body(error);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleMethodValidationException(HandlerMethodValidationException ex,
                                                                            HttpServletRequest request) {
        ApiErrorResponse error = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                ex.getMessage(),
                request.getRequestURI(),
                LocalDateTime.now().toString()
        );
        return ResponseEntity.status(error.getStatus()).body(error);
    }
}


