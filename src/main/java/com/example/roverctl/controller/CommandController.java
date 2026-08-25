package com.example.roverctl.controller;

import com.example.roverctl.dto.response.CommandResponse;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.CommandType;
import com.example.roverctl.service.CommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/commands")
@RequiredArgsConstructor
public class CommandController {

    private final CommandService commandService;

    @GetMapping
    public Page<CommandResponse> getAll(
            @RequestParam(required = false) String roverName,
            @RequestParam(required = false) CommandStatus status,
            Pageable pageable) {
        return commandService.findAll(roverName, status, pageable).map(CommandResponse::from);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelCommand(@PathVariable Long id) {
        commandService.cancel(id);
    }

    @PostMapping("/test-rollback")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void testRollback(@RequestParam String roverName, @RequestParam CommandType type) {
        commandService.testTransactionRollback(roverName, type);
    }

    @GetMapping("/{id}")
    public CommandResponse getById(@PathVariable Long id) {
        return CommandResponse.from(commandService.findById(id));
    }
}