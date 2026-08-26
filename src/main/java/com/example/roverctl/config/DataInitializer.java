package com.example.roverctl.config;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import com.example.roverctl.repository.RoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
@Order(1)
public class DataInitializer implements CommandLineRunner {

    private final RoverRepository roverRepository;
    private final Clock clock;

    @Override
    public void run(String... args) {
        if (roverRepository.count() > 0) {
            return;
        }

        roverRepository.save(Rover.builder()
                .name("Perseverance").missionId("MARS-2020")
                .status(RoverStatus.OPERATIONAL).batteryPercent(87)
                .positionX(18.44).positionY(77.45)
                .lastContactAt(Instant.now(clock))
                .build());

        roverRepository.save(Rover.builder()
                .name("Curiosity").missionId("MARS-2011")
                .status(RoverStatus.OPERATIONAL).batteryPercent(20)
                .positionX(-4.5).positionY(137.4)
                .lastContactAt(Instant.now(clock))
                .build());

        roverRepository.save(Rover.builder()
                .name("Opportunity").missionId("MER-B")
                .status(RoverStatus.LOST).batteryPercent(0)
                .positionX(1.9).positionY(354.5)
                .lastContactAt(Instant.now(clock).minus(Duration.ofDays(2000)))
                .build());
    }
}