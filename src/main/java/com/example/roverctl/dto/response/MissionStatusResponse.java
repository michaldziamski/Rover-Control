package com.example.roverctl.dto.response;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public record MissionStatusResponse(
        String missionName,
        long signalDelayMinutes,
        Map<String, Long> roversByStatus,
        long commandsInTransit,
        Instant nextExpectedAck,
        List<String> silentRovers
) {
}