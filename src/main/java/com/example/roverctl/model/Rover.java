package com.example.roverctl.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Rover {

    private String name;
    private String missionId;
    private RoverStatus status;
    private int batteryPercent;
    private double positionX;
    private double positionY;
    private Instant lastContactAt;
}