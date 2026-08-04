package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Component
@Primary
@RequiredArgsConstructor
public class OrbitalDelayCalculator implements SignalDelayCalculator {

    private static final double SYNODIC_PERIOD_DAYS = 779.9;

    private final MissionProperties missionProperties;

    @Override
    public Duration oneWayDelay() {
        long daysSinceEpoch = ChronoUnit.DAYS.between(Instant.EPOCH, Instant.now());
        double phase = (daysSinceEpoch % SYNODIC_PERIOD_DAYS) / SYNODIC_PERIOD_DAYS;

        double oscillation = (1 - Math.cos(2 * Math.PI * phase)) / 2;

        int min = missionProperties.getMinSignalDelayMinutes();
        int max = missionProperties.getMaxSignalDelayMinutes();
        long minutes = Math.round(min + oscillation * (max - min));

        return Duration.ofMinutes(minutes);
    }
}