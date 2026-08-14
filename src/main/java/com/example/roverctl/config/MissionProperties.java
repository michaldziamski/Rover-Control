package com.example.roverctl.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "mission")
@Getter
@Setter
public class MissionProperties {

    private String name;
    private int minSignalDelayMinutes;
    private int maxSignalDelayMinutes;
    private int lowBatteryThreshold;
    private int drillBatteryRequirement;
    private int maxCommandsPerRover;
}