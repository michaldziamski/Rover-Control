package com.example.roverctl.reporter;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import com.example.roverctl.service.RoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FleetHealthReporter {

    private final RoverService roverService;

    public void logFleetSummary() {
        List<Rover> rovers = roverService.findAll();

        Map<RoverStatus, Long> statusCount = rovers.stream()
                .collect(Collectors.groupingBy(
                        Rover::getStatus,
                        Collectors.counting()
                ));

        double averageBattery = rovers.stream()
                .mapToInt(Rover::getBatteryPercent)
                .average()
                .orElse(0);

        long lostCount = rovers.stream()
                .filter(rover -> rover.getStatus() == RoverStatus.LOST)
                .count();

        log.info("--- Fleet Health Report ---");
        log.info("Rovers by status: {}", statusCount);
        log.info("Average battery: {}%", averageBattery);
        log.info("Lost rovers: {}", lostCount);
    }
}