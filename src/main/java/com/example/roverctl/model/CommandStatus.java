package com.example.roverctl.model;

public enum CommandStatus {
    QUEUED,
    IN_TRANSIT,
    EXECUTING,
    ACKNOWLEDGED,
    FAILED
}