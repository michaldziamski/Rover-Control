package com.example.roverctl.repository;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
public class RoverRepository {

    private final Map<String, Rover> rovers = new ConcurrentHashMap<>();

    @PostConstruct
    void seedInitialFleet() {
        save(Rover.builder()
                .name("Perseverance")
                .missionId("MARS-2020")
                .status(RoverStatus.OPERATIONAL)
                .batteryPercent(87)
                .positionX(18.44)
                .positionY(77.45)
                .lastContactAt(Instant.now())
                .build());

        save(Rover.builder()
                .name("Curiosity")
                .missionId("MSL-2011")
                .status(RoverStatus.DEGRADED)
                .batteryPercent(41)
                .positionX(-4.59)
                .positionY(137.44)
                .lastContactAt(Instant.now())
                .build());

        save(Rover.builder()
                .name("Opportunity")
                .missionId("MER-B")
                .status(RoverStatus.LOST)
                .batteryPercent(0)
                .positionX(-1.95)
                .positionY(354.47)
                .lastContactAt(Instant.now())
                .build());

        log.info("Seeded fleet with {} rovers", rovers.size());
    }

    public Rover save(Rover rover) {
        rovers.put(rover.getName(), rover);
        return rover;
    }

    public Optional<Rover> findByName(String name) {
        return Optional.ofNullable(rovers.get(name));
    }

    public List<Rover> findAll() {
        return List.copyOf(rovers.values());
    }
}