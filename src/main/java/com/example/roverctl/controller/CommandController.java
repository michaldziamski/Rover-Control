package com.example.roverctl.controller;

import com.example.roverctl.dto.response.CommandResponse;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @GetMapping
    public List<CommandResponse> getAll(
            @RequestParam(required = false) String roverName,
            @RequestParam(required = false) CommandStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return commandService.findAll(roverName, status)
                .stream()
                .skip((long) page * size)
                .limit(size)
                .map(CommandResponse::from)
                .toList();
    }

    @GetMapping("/rovers/{name}/commands/pending")
    public List<CommandResponse> getPendingCommands(
            @PathVariable String name) {

        return commandService.findPendingForRover(name)
                .stream()
                .map(CommandResponse::from)
                .toList();
    }

    @DeleteMapping("/commands/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelCommand(@PathVariable UUID id) {
        commandService.cancel(id);
    }

    @PostMapping("/rovers/{name}/emergency-hibernate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CommandResponse emergencyHibernate(
            @PathVariable String name) {

        return CommandResponse.from(
                commandService.emergencyHibernate(name)
        );
    }
}