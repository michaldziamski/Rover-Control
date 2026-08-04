package com.example.roverctl.service;

import com.example.roverctl.config.MissionProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class FixedDelayCalculator implements SignalDelayCalculator {

    private final MissionProperties missionProperties;

    @Override
    public Duration oneWayDelay() {
        return Duration.ofMinutes(missionProperties.getMinSignalDelayMinutes());
    }
}