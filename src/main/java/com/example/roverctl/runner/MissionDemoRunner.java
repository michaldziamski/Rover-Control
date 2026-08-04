package com.example.roverctl.runner;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.exception.CommandRejectedException;
import com.example.roverctl.model.CommandType;
import com.example.roverctl.service.CommandService;
import com.example.roverctl.service.RoverService;
import com.example.roverctl.service.SignalDelayCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.example.roverctl.reporter.FleetHealthReporter;

@Component
@RequiredArgsConstructor
@Slf4j
public class MissionDemoRunner implements CommandLineRunner {

    private final RoverService roverService;
    private final CommandService commandService;
    private final SignalDelayCalculator delayCalculator;
    private final MissionProperties missionProperties;
    private final FleetHealthReporter fleetHealthReporter;

    @Override
    public void run(String... args) {
        log.info("=== {} ===", missionProperties.getName());
        log.info("Current one-way signal delay: {} minutes", delayCalculator.oneWayDelay().toMinutes());

        log.info("--- Fleet status ---");
        roverService.findAll().forEach(rover ->
                log.info("{} [{}] battery={}% at ({}, {})",
                        rover.getName(), rover.getStatus(), rover.getBatteryPercent(),
                        rover.getPositionX(), rover.getPositionY()));

        log.info("--- Sending commands ---");
        trySend("Perseverance", CommandType.TAKE_PHOTO);
        trySend("Perseverance", CommandType.DRILL);
        trySend("Curiosity", CommandType.DRILL);        // za mało baterii na wiercenie
        trySend("Opportunity", CommandType.WAKE);       // LOST — odrzucone
        trySend("Voyager", CommandType.DRIVE);          // nie istnieje

        log.info("--- Command log: {} entries ---", commandService.getCommandLog().size());
        log.info("--- Command status ---");
        commandService.getCommandLog().forEach(command ->
                log.info("Command {} for {}: {}",
                        command.getType(),
                        command.getRoverName(),
                        command.hasArrivedOnMars() ? "ARRIVED ON MARS" : "IN TRANSIT"));

        log.info("--- Fleet status ---");
        roverService.findAll().forEach(rover ->
                log.info("{} [{}] battery={}% at ({}, {})",
                        rover.getName(), rover.getStatus(), rover.getBatteryPercent(),
                        rover.getPositionX(), rover.getPositionY()));

        fleetHealthReporter.logFleetSummary();
    }

    private void trySend(String roverName, CommandType type) {
        try {
            commandService.sendCommand(roverName, type);
        } catch (/*CommandRejectedException | */RuntimeException e) {
            log.warn("Command {} to {} rejected: {}", type, roverName, e.getMessage());
        }
    }
}