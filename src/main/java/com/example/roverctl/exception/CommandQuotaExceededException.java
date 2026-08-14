package com.example.roverctl.exception;

public class CommandQuotaExceededException extends RuntimeException {
    public CommandQuotaExceededException(String message) {
        super(message);
    }
}