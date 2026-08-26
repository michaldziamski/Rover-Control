package com.example.roverctl;

import com.example.roverctl.model.Rover;
import com.example.roverctl.repository.CommandRepository;
import com.example.roverctl.repository.RoverRepository;
import com.example.roverctl.support.RollbackTestHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CommandServiceRollbackTest {

    @Autowired private RollbackTestHelper rollbackTestHelper;
    @Autowired private CommandRepository commandRepository;
    @Autowired private RoverRepository roverRepository;

    @Test
    void rollsBackAllChangesOnFailure() {
        Rover rover = roverRepository.findAll().get(0); // dowolny istniejący łazik
        Instant lastContactBefore = rover.getLastContactAt();
        long commandCountBefore = commandRepository.count();

        assertThrows(IllegalStateException.class, () ->
                rollbackTestHelper.multiWriteThenFail(rover.getName()));

        assertEquals(commandCountBefore, commandRepository.count());

        Rover roverAfter = roverRepository.findByName(rover.getName()).orElseThrow();
        assertEquals(lastContactBefore, roverAfter.getLastContactAt());
    }
}