package com.example.roverctl.repository;

import com.example.roverctl.model.Rover;
import com.example.roverctl.model.RoverStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoverRepositoryTest {

    @Autowired
    private RoverRepository roverRepository;

    @Test
    void shouldFindRoverByName() {
        Rover rover = Rover.builder()
                .name("Perseverance")
                .missionId("MARS-001")
                .status(RoverStatus.OPERATIONAL)
                .batteryPercent(87)
                .positionX(10.5)
                .positionY(20.5)
                .lastContactAt(Instant.now())
                .build();

        roverRepository.save(rover);

        Optional<Rover> result =
                roverRepository.findByName("Perseverance");

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Perseverance");
        assertThat(result.get().getMissionId()).isEqualTo("MARS-001");
    }

    @Test
    void shouldFindRoversByStatus() {
        Rover rover1 = Rover.builder()
                .name("Perseverance")
                .missionId("MARS-001")
                .status(RoverStatus.OPERATIONAL)
                .batteryPercent(87)
                .positionX(10.5)
                .positionY(20.5)
                .lastContactAt(Instant.now())
                .build();

        Rover rover2 = Rover.builder()
                .name("Curiosity")
                .missionId("MARS-002")
                .status(RoverStatus.OPERATIONAL)
                .batteryPercent(75)
                .positionX(15.0)
                .positionY(25.0)
                .lastContactAt(Instant.now())
                .build();

        Rover rover3 = Rover.builder()
                .name("Spirit")
                .missionId("MARS-003")
                .status(RoverStatus.LOST)
                .batteryPercent(0)
                .positionX(30.0)
                .positionY(40.0)
                .lastContactAt(Instant.now())
                .build();

        roverRepository.saveAll(List.of(rover1, rover2, rover3));

        List<Rover> result =
                roverRepository.findByStatus(RoverStatus.OPERATIONAL);

        assertThat(result)
                .hasSize(2)
                .extracting(Rover::getName)
                .containsExactlyInAnyOrder(
                        "Perseverance",
                        "Curiosity"
                );
    }

    @Test
    void shouldFindSilentRovers() {
        Instant now = Instant.now();
        Instant cutoff = now.minusSeconds(3600);

        Rover silentRover = Rover.builder()
                .name("Spirit")
                .missionId("MARS-003")
                .status(RoverStatus.LOST)
                .batteryPercent(20)
                .positionX(30.0)
                .positionY(40.0)
                .lastContactAt(now.minusSeconds(7200))
                .build();

        Rover activeRover = Rover.builder()
                .name("Perseverance")
                .missionId("MARS-001")
                .status(RoverStatus.OPERATIONAL)
                .batteryPercent(87)
                .positionX(10.5)
                .positionY(20.5)
                .lastContactAt(now.minusSeconds(600))
                .build();

        roverRepository.saveAll(List.of(silentRover, activeRover));

        List<Rover> result =
                roverRepository.findByLastContactAtBefore(cutoff);

        assertThat(result)
                .hasSize(1)
                .extracting(Rover::getName)
                .containsExactly("Spirit");
    }
}