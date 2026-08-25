package com.example.roverctl.dto.response;

import com.example.roverctl.model.TelemetryPacket;

import java.time.Instant;

public record TelemetryPacketResponse(
        Long id,
        String roverName,
        double temperatureCelsius,
        int batteryPercent,
        Instant recordedAt,
        Instant receivedAt
) {

    public static TelemetryPacketResponse from(TelemetryPacket packet) {
        return new TelemetryPacketResponse(
                packet.getId(),
                packet.getRover().getName(),
                packet.getTemperatureCelsius(),
                packet.getBatteryPercent(),
                packet.getRecordedAt(),
                packet.getReceivedAt()
        );
    }
}