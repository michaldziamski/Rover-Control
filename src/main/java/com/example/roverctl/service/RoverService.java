package com.example.roverctl.service;

import com.example.roverctl.exception.RoverNotFoundException;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import com.example.roverctl.repository.RoverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoverService {

    private final RoverRepository roverRepository;

    @Transactional(readOnly = true)
    public List<Rover> findAll() {
        return roverRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Rover getByName(String name) {
        return roverRepository.findByName(name)
                .orElseThrow(() -> new RoverNotFoundException("Rover not found: " + name));
    }

    @Transactional(readOnly = true)
    public List<Rover> findByStatus(RoverStatus status) {
        return roverRepository.findByStatus(status);
    }

    @Transactional
    public Rover updateBattery(String name, int percent) {
        if (percent < 0 || percent > 100) {
            throw new IllegalArgumentException(
                    "Battery must be between 0 and 100");

        }
        Rover rover = getByName(name);
        rover.setBatteryPercent(percent);

        return rover;
    }

    @Transactional(readOnly = true)
    public List<Rover> findSilentRovers(Instant threshold) {
        return roverRepository.findByLastContactAtBefore(threshold);
    }
}