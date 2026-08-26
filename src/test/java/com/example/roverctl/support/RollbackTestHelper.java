package com.example.roverctl.support;

import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import com.example.roverctl.model.CommandType;
import com.example.roverctl.model.Rover;
import com.example.roverctl.repository.CommandRepository;
import com.example.roverctl.repository.RoverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class RollbackTestHelper {

    private final RoverRepository roverRepository;
    private final CommandRepository commandRepository;

    @Transactional
    public void multiWriteThenFail(String roverName) {
        Rover rover = roverRepository.findByName(roverName).orElseThrow();
        rover.setLastContactAt(Instant.now()); // zapis #1 — dirty checking, flush przy commicie

        Command command = Command.builder()
                .rover(rover)
                .type(CommandType.DRIVE)
                .status(CommandStatus.IN_TRANSIT)
                .earthSentAt(Instant.now())
                .marsArrivalAt(Instant.now().plusSeconds(600))
                .ackExpectedAt(Instant.now().plusSeconds(1200))
                .build();
        commandRepository.save(command); // zapis #2

        throw new IllegalStateException("forced failure for rollback test");
    }
}