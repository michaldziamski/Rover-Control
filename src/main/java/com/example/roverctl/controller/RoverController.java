package com.example.roverctl.controller;

import com.example.roverctl.dto.request.UpdateBatteryRequest;
import com.example.roverctl.dto.response.RoverResponse;
import com.example.roverctl.model.Rover;
import com.example.roverctl.service.RoverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.example.roverctl.model.RoverStatus;

import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/api/rovers")
@RequiredArgsConstructor
public class RoverController {

    private final RoverService roverService;

    /*@GetMapping
    public List<RoverResponse> getAll() {
        return roverService.findAll().stream()
                .map(RoverResponse::from)
                .toList();
    }*/

    @GetMapping
    public List<RoverResponse> getAll(
            @RequestParam(required = false) RoverStatus status) {

        List<Rover> rovers;

        if (status == null) {
            rovers = roverService.findAll();
        } else {
            rovers = roverService.findByStatus(status);
        }

        return rovers.stream()
                .map(RoverResponse::from)
                .toList();
        }

    @GetMapping("/{name}")
    public RoverResponse getByName(@PathVariable String name) {
        return RoverResponse.from(
                roverService.getByName(name)
        );
    }

    @PatchMapping("/{name}/battery")
    public RoverResponse updateBattery(
            @PathVariable String name,
            @Valid @RequestBody UpdateBatteryRequest request) {

        Rover rover = roverService.updateBattery(
                name,
                request.batteryPercent()
        );

        return RoverResponse.from(rover);
    }

}

