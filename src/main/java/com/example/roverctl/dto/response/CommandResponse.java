package com.example.roverctl.dto.response;

import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.CommandType;

import java.time.Duration;
import java.time.Instant;

public record CommandResponse(
        Long id,
        String roverName,
        CommandType type,
        CommandStatus status,
        Instant earthSentAt,
        Instant marsArrivalAt,
        Instant ackExpectedAt,
        long minutesUntilArrival
) {

    public static CommandResponse from(Command command, Instant now) {

        long minutesUntilArrival = Math.max(
                0,
                Duration.between(
                        now,
                        command.getMarsArrivalAt()
                ).toMinutes()
        );

        return new CommandResponse(
                command.getId(),
                command.getRover().getName(),
                command.getType(),
                command.getStatus(),
                command.getEarthSentAt(),
                command.getMarsArrivalAt(),
                command.getAckExpectedAt(),
                minutesUntilArrival
        );
    }

    public static CommandResponse from(Command command) {
        return from(command, Instant.now());
    }
}