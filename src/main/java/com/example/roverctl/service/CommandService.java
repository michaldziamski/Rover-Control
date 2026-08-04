package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.exception.CommandRejectedException;
import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.CommandType;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final RoverService roverService;
    private final SignalDelayCalculator delayCalculator;
    private final MissionProperties missionProperties;

    private final List<Command> commandLog = new ArrayList<>();

    public Command sendCommand(String roverName, CommandType type) {
        Rover rover = roverService.getByName(roverName);

        validateCommand(rover, type);

        Duration delay = delayCalculator.oneWayDelay();
        Instant now = Instant.now();

        Command command = Command.builder()
                .id(UUID.randomUUID())
                .roverName(rover.getName())
                .type(type)
                .status(CommandStatus.IN_TRANSIT)
                .earthSentAt(now)
                .marsArrivalAt(now.plus(delay))
                .ackExpectedAt(now.plus(delayCalculator.roundTripDelay()))
                .build();

        commandLog.add(command);

        log.info("Command {} ({}) sent to {} — arrives on Mars in {} min, ack expected in {} min",
                command.getId(), type, rover.getName(),
                delay.toMinutes(), delayCalculator.roundTripDelay().toMinutes());

        return command;
    }

    private void validateCommand(Rover rover, CommandType type) {
        if (rover.getStatus() == RoverStatus.LOST) {
            throw new CommandRejectedException(
                    "Rover " + rover.getName() + " is LOST — no contact possible");
        }

        if (rover.getStatus() == RoverStatus.HIBERNATING && type != CommandType.WAKE) {
            throw new CommandRejectedException(
                    "Rover " + rover.getName() + " is hibernating — only WAKE accepted");
        }

        if (rover.getBatteryPercent() < missionProperties.getLowBatteryThreshold()
                && type != CommandType.HIBERNATE) {
            throw new CommandRejectedException(
                    "Battery too low (" + rover.getBatteryPercent() + "%) for command " + type);
        }

        if (type == CommandType.DRILL
                && rover.getBatteryPercent() < missionProperties.getDrillBatteryRequirement()) {
            throw new CommandRejectedException(
                    "DRILL requires at least " + missionProperties.getDrillBatteryRequirement()
                            + "% battery, rover has " + rover.getBatteryPercent() + "%");
        }
    }

    public List<Command> getCommandLog() {
        return List.copyOf(commandLog);
    }

    // 9.2
    public List<Command> getCommandsForRover(String roverName) {
        return commandLog.stream()
                .filter(command -> command.getRoverName().equals(roverName))
                .toList();
    }
}