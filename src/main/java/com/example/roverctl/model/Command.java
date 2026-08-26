package com.example.roverctl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "commands")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Command {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rover_id", nullable = false)
    private Rover rover;

    @Enumerated(EnumType.STRING)
    private CommandType type;
    @Enumerated(EnumType.STRING)
    private CommandStatus status;

    private Instant earthSentAt;
    private Instant marsArrivalAt;
    private Instant ackExpectedAt;

    public boolean hasArrivedOnMars(Instant now) {
        return now.isAfter(marsArrivalAt);
    }
}