package com.example.roverctl.controller;

import com.example.roverctl.dto.request.SendCommandRequest;
import com.example.roverctl.dto.request.UpdateBatteryRequest;
import com.example.roverctl.dto.response.CommandResponse;
import com.example.roverctl.dto.response.RoverResponse;
import com.example.roverctl.model.Command;
import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import com.example.roverctl.service.CommandService;
import com.example.roverctl.service.RoverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rovers")
@RequiredArgsConstructor
public class RoverController {

    private final RoverService roverService;
    private final CommandService commandService;


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

    @PostMapping("/{name}/commands")
    public ResponseEntity<CommandResponse> sendCommand(
            @PathVariable String name,
            @Valid @RequestBody SendCommandRequest request) {

        Command command = commandService.sendCommand(name, request.type());
        CommandResponse body = CommandResponse.from(command);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .header("Location", "/api/commands/" + command.getId())
                .body(body);
    }

    @GetMapping("/{name}/commands")
    public List<CommandResponse> getCommandHistory(@PathVariable String name) {
        return commandService.getCommandsForRover(name).stream()
                .map(CommandResponse::from)
                .toList();
    }

    @GetMapping("/{name}/commands/pending")
    public List<CommandResponse> getPendingCommands(@PathVariable String name) {
        return commandService.findPendingForRover(name).stream()
                .map(CommandResponse::from)
                .toList();
    }

    @PostMapping("/{name}/emergency-hibernate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommandResponse emergencyHibernate(@PathVariable String name) {
        return CommandResponse.from(commandService.emergencyHibernate(name));
    }

}

