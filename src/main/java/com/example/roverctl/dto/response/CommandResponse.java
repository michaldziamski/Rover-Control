package com.example.roverctl.dto.response;

import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.CommandType;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record CommandResponse(
        UUID id,
        String roverName,
        CommandType type,
        CommandStatus status,
        Instant earthSentAt,
        Instant marsArrivalAt,
        Instant ackExpectedAt,
        long minutesUntilArrival
) {

    public static CommandResponse from(Command command) {

        long minutesUntilArrival = Math.max(
                0,
                Duration.between(
                        Instant.now(),
                        command.getMarsArrivalAt()
                ).toMinutes()
        );

        return new CommandResponse(
                command.getId(),
                command.getRoverName(),
                command.getType(),
                command.getStatus(),
                command.getEarthSentAt(),
                command.getMarsArrivalAt(),
                command.getAckExpectedAt(),
                minutesUntilArrival
        );
    }
}