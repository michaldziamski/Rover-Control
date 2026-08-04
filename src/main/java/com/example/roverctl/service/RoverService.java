package com.example.roverctl.service;

import com.example.roverctl.exception.RoverNotFoundException;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import com.example.roverctl.repository.RoverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoverService {

    private final RoverRepository roverRepository;

    public List<Rover> findAll() {
        return roverRepository.findAll();
    }

    public Rover getByName(String name) {
        return roverRepository.findByName(name)
                .orElseThrow(() -> new RoverNotFoundException("Rover not found: " + name));
    }

    public List<Rover> findByStatus(RoverStatus status) {
        return roverRepository.findAll().stream()
                .filter(rover -> rover.getStatus().equals(status))
                .toList();
    }
}