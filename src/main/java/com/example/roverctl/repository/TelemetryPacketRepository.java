package com.example.roverctl.repository;

import com.example.roverctl.model.TelemetryPacket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface TelemetryPacketRepository
        extends JpaRepository<TelemetryPacket, Long> {

    @EntityGraph(attributePaths = "rover")
    List<TelemetryPacket> findByRoverNameOrderByRecordedAtDesc(String name);

    @EntityGraph(attributePaths = "rover")
    List<TelemetryPacket> findByRoverNameAndRecordedAtAfterOrderByRecordedAtDesc(
            String name,
            Instant since
    );
}