package com.example.roverctl.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@ToString
@Builder
public class Command {

    private UUID id;
    private String roverName;
    private CommandType type;
    private CommandStatus status;

    private Instant earthSentAt;
    private Instant marsArrivalAt;
    private Instant ackExpectedAt;

    public boolean hasArrivedOnMars() {
        return !marsArrivalAt.isAfter(Instant.now());
    }
}