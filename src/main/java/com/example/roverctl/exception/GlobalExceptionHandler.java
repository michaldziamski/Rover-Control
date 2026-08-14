package com.example.roverctl.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.time.Instant;
import java.util.stream.Collectors;
import com.example.roverctl.model.CommandType;
import java.util.Arrays;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RoverNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoverNotFound(
            RoverNotFoundException e) {

        log.warn("Rover not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "ROVER_NOT_FOUND",
                        e.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(CommandNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommandNotFound(
            CommandNotFoundException e) {

        log.warn("Command not found: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "COMMAND_NOT_FOUND",
                        e.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(CommandRejectedException.class)
    public ResponseEntity<ErrorResponse> handleCommandRejected(
            CommandRejectedException e) {

        log.warn("Command rejected: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "COMMAND_REJECTED",
                        e.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(CommandQuotaExceededException.class)
    public ResponseEntity<ErrorResponse> handleCommandQuotaExceeded(
            CommandQuotaExceededException e) {

        log.warn("Command quota exceeded: {}", e.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "QUOTA_EXCEEDED",
                        e.getMessage(),
                        Instant.now()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error ->
                        error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        log.warn("Validation failed: {}", message);

        return ResponseEntity
                .status(422)
                .body(new ErrorResponse(
                        "VALIDATION_FAILED",
                        message,
                        Instant.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(
            Exception e) {

        log.error("Unexpected error", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_ERROR",
                        "Unexpected error occurred",
                        Instant.now()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(
            HttpMessageNotReadableException e) {

        log.warn("Invalid request body: {}", e.getMessage());

        String message = "Invalid enum value. Allowed values: "
                + String.join(", ",
                Arrays.stream(CommandType.values())
                        .map(Enum::name)
                        .toList());

        return ResponseEntity
                .status(422)
                .body(new ErrorResponse(
                        "VALIDATION_FAILED",
                        message,
                        Instant.now()
                ));
    }
}