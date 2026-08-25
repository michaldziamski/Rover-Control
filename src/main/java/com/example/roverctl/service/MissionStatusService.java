package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.dto.response.MissionStatusResponse;
import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.Rover;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MissionStatusService {

    private final RoverService roverService;
    private final CommandService commandService;
    private final SignalDelayCalculator signalDelayCalculator;
    private final MissionProperties missionProperties;
    private final Clock clock;

    public MissionStatusResponse getStatus() {

        List<Rover> rovers = roverService.findAll();

        Map<String, Long> roversByStatus = rovers.stream()
                .collect(Collectors.groupingBy(
                        rover -> rover.getStatus().name(),
                        Collectors.counting()
                ));

        List<Command> commandsInTransit = commandService.findByStatus(
                CommandStatus.IN_TRANSIT
        );

        Duration delay = signalDelayCalculator.oneWayDelay();

        Instant nextExpectedAck = commandsInTransit.stream()
                .map(Command::getAckExpectedAt)
                .min(Instant::compareTo)
                .orElse(null);

        Instant silentThreshold = Instant.now(clock)
                .minus(Duration.ofHours(
                        missionProperties.getSilentRoverHours()
                ));

        List<String> silentRovers = roverService
                .findSilentRovers(silentThreshold)
                .stream()
                .map(Rover::getName)
                .toList();

        return new MissionStatusResponse(
                missionProperties.getName(),
                delay.toMinutes(),
                roversByStatus,
                commandsInTransit.size(),
                nextExpectedAck,
                silentRovers
        );
    }
}