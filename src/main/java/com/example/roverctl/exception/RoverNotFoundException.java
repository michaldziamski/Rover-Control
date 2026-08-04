package com.example.roverctl.exception;

public class RoverNotFoundException extends RuntimeException {
    public RoverNotFoundException(String message) {
        super(message);
    }
}