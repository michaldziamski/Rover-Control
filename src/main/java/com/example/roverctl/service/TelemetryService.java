package com.example.roverctl.service;

import com.example.roverctl.dto.request.TelemetryPacketRequest;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.TelemetryPacket;
import com.example.roverctl.repository.TelemetryPacketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelemetryService {

    private final RoverService roverService;
    private final TelemetryPacketRepository telemetryPacketRepository;
    private final Clock clock;

    @Transactional
    public TelemetryPacket receiveTelemetry(
            String roverName,
            TelemetryPacketRequest request) {

        Rover rover = roverService.getByName(roverName);

        TelemetryPacket packet = TelemetryPacket.builder()
                .rover(rover)
                .temperatureCelsius(request.temperatureCelsius())
                .batteryPercent(request.batteryPercent())
                .recordedAt(request.recordedAt())
                .receivedAt(Instant.now(clock))
                .build();

        return telemetryPacketRepository.save(packet);
    }

    @Transactional(readOnly = true)
    public List<TelemetryPacket> findTelemetry(
            String roverName,
            Instant since) {

        roverService.getByName(roverName);

        if (since != null) {
            return telemetryPacketRepository
                    .findByRoverNameAndRecordedAtAfterOrderByRecordedAtDesc(
                            roverName,
                            since
                    );
        }

        return telemetryPacketRepository
                .findByRoverNameOrderByRecordedAtDesc(roverName);
    }
}