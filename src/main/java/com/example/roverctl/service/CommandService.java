package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.exception.CommandNotFoundException;
import com.example.roverctl.exception.CommandQuotaExceededException;
import com.example.roverctl.exception.CommandRejectedException;
import com.example.roverctl.model.*;
import com.example.roverctl.repository.CommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommandService {

    private final RoverService roverService;
    private final SignalDelayCalculator delayCalculator;
    private final MissionProperties missionProperties;
    private final Clock clock;

    private final CommandRepository commandRepository;

    @Transactional
    public Command sendCommand(String roverName, CommandType type) {
        Rover rover = roverService.getByName(roverName);

        long commandsForRover = commandRepository.countByRoverName(roverName);
        if (commandsForRover >= missionProperties.getMaxCommandsPerRover()) {
            throw new CommandQuotaExceededException(
                    "Rover " + roverName + " has reached the command limit of "
                            + missionProperties.getMaxCommandsPerRover());
        }

        validateCommand(rover, type);

        Command command = buildCommand(rover, type);
        commandRepository.save(command);

        log.info("Command {} ({}) sent to {}", command.getId(), type, rover.getName());

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

    private Command buildCommand(Rover rover, CommandType type) {
        Instant now = Instant.now(clock);
        Duration delay = delayCalculator.oneWayDelay();
        Duration roundTrip = delayCalculator.roundTripDelay();

        return Command.builder()
                .rover(rover)
                .type(type)
                .status(CommandStatus.IN_TRANSIT)
                .earthSentAt(now)
                .marsArrivalAt(now.plus(delay))
                .ackExpectedAt(now.plus(roundTrip))
                .build();
    }

    @Transactional(readOnly = true)
    public List<Command> getCommandLog() {
        return commandRepository.findAllWithRover();
    }

    @Transactional(readOnly = true)
    public List<Command> findByStatus(CommandStatus status) {
        return commandRepository.findByStatusWithRover(status);
    }

    @Transactional(readOnly = true)
    public Command findById(Long id) {
        return commandRepository.findById(id)
                .orElseThrow(() ->
                        new CommandNotFoundException(
                                "Command not found: " + id));
    }

    @Transactional(readOnly = true)
    public List<Command> getCommandsForRover(String roverName) {
        roverService.getByName(roverName);
        return commandRepository.findByRoverNameWithRover(roverName);
    }

    @Transactional(readOnly = true)
    public Page<Command> findAll(
            String roverName,
            CommandStatus status,
            Pageable pageable) {

        if (roverName != null && status != null) {
            return commandRepository.findByRoverNameAndStatus(
                    roverName,
                    status,
                    pageable
            );
        }

        if (roverName != null) {
            return commandRepository.findByRoverName(
                    roverName,
                    pageable
            );
        }

        if (status != null) {
            return commandRepository.findByStatus(
                    status,
                    pageable
            );
        }

        return commandRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<Command> findPendingForRover(String roverName) {
        roverService.getByName(roverName);

        return commandRepository.findPendingByRoverName(
                roverName,
                Instant.now(clock)
        );
    }

    private Command getCommandOrThrow(Long id) {
        return commandRepository.findById(id)
                .orElseThrow(() -> new CommandNotFoundException("Command not found: " + id));
    }

    @Transactional
    public void cancel(Long id) {
        Command command = getCommandOrThrow(id);
        Instant now = Instant.now(clock);

        if (command.hasArrivedOnMars(now)) {
            throw new CommandRejectedException(
                    "Command has already arrived on Mars and cannot be cancelled");
        }

        commandRepository.delete(command);
    }

    @Transactional
    public Command emergencyHibernate(String roverName) {
        Rover rover = roverService.getByName(roverName);

        Command command = buildCommand(rover, CommandType.HIBERNATE);
        commandRepository.save(command);

        return command;
    }
}