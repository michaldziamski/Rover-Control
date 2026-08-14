package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.dto.response.MissionStatusResponse;
import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public MissionStatusResponse getStatus() {
        List<Rover> rovers = roverService.findAll();

        Map<String, Long> roversByStatus = rovers.stream()
                .collect(Collectors.groupingBy(
                        rover -> rover.getStatus().name(),
                        Collectors.counting()
                ));

        long commandsInTransit = commandService.findAll(
                null,
                CommandStatus.IN_TRANSIT
        ).size();

        Duration delay = signalDelayCalculator.oneWayDelay();

        Instant nextExpectedAck = commandService
                .findAll(null, CommandStatus.IN_TRANSIT)
                .stream()
                .map(Command::getAckExpectedAt)
                .min(Instant::compareTo)
                .orElse(null);

        return new MissionStatusResponse(
                missionProperties.getName(),
                delay.toMinutes(),
                roversByStatus,
                commandsInTransit,
                nextExpectedAck
        );
    }

}