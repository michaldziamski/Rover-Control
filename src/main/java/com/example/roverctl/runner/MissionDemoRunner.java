package com.example.roverctl.runner;

import com.example.roverctl.config.MissionProperties;
import com.example.roverctl.service.RoverService;
import com.example.roverctl.service.SignalDelayCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class MissionDemoRunner implements CommandLineRunner {

    private final RoverService roverService;
    private final SignalDelayCalculator delayCalculator;
    private final MissionProperties missionProperties;

    @Override
    public void run(String... args) {
        log.info("=== {} ===", missionProperties.getName());
        log.info("Current one-way signal delay: {} minutes",
                delayCalculator.oneWayDelay().toMinutes());

        log.info("--- Fleet status ---");

        roverService.findAll().forEach(rover ->
                log.info("{} [{}] battery={}% at ({}, {})",
                        rover.getName(),
                        rover.getStatus(),
                        rover.getBatteryPercent(),
                        rover.getPositionX(),
                        rover.getPositionY()));
    }
}