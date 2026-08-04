package com.example.roverctl.exception;

public class CommandRejectedException extends RuntimeException {
    public CommandRejectedException(String message) {
        super(message);
    }
}