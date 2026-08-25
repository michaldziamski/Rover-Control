package com.example.roverctl.dto.request;

import java.time.Instant;

public record TelemetryPacketRequest(
        double temperatureCelsius,
        int batteryPercent,
        Instant recordedAt
) {
}