package com.example.roverctl.exception;

public class CommunicationBlackoutException extends RuntimeException {
    public CommunicationBlackoutException(String message) {
        super(message);
    }
}