package com.example.roverctl.repository;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RoverRepository extends JpaRepository<Rover, Long> {

    Optional<Rover> findByName(String name);

    List<Rover> findByStatus(RoverStatus status);

    List<Rover> findByLastContactAtBefore(Instant threshold);
}