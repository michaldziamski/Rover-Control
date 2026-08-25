package com.example.roverctl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "telemetry_packets")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TelemetryPacket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rover_id", nullable = false)
    private Rover rover;

    private double temperatureCelsius;

    private int batteryPercent;

    private Instant recordedAt;

    private Instant receivedAt;
}