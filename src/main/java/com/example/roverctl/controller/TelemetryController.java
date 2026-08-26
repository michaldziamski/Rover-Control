package com.example.roverctl.controller;

import com.example.roverctl.dto.request.TelemetryPacketRequest;
import com.example.roverctl.dto.response.TelemetryPacketResponse;
import com.example.roverctl.service.TelemetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/v1/rovers/{name}/telemetry")
@RequiredArgsConstructor
public class TelemetryController {

    private final TelemetryService telemetryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TelemetryPacketResponse receiveTelemetry(
            @PathVariable String name,
            @Valid @RequestBody TelemetryPacketRequest request) {

        return TelemetryPacketResponse.from(
                telemetryService.receiveTelemetry(name, request)
        );
    }

    @GetMapping
    public List<TelemetryPacketResponse> getTelemetry(
            @PathVariable String name,
            @RequestParam(required = false) Instant since) {

        return telemetryService.findTelemetry(name, since)
                .stream()
                .map(TelemetryPacketResponse::from)
                .toList();
    }
}