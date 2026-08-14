package com.example.roverctl.dto.response;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;

public record RoverResponse(
        String name,
        String missionId,
        RoverStatus status,
        int batteryPercent,
        double positionX,
        double positionY
) {
    public static RoverResponse from(Rover rover) {
        return new RoverResponse(
                rover.getName(), rover.getMissionId(), rover.getStatus(),
                rover.getBatteryPercent(), rover.getPositionX(), rover.getPositionY());
    }
}