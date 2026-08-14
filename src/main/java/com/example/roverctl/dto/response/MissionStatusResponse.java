package com.example.roverctl.dto.response;

import java.util.Map;
import java.time.Instant;

public record MissionStatusResponse(
        String missionName,
        long signalDelayMinutes,
        Map<String, Long> roversByStatus,
        long commandsInTransit,
        Instant nextExpectedAck
) {
}