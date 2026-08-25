package com.example.roverctl.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rovers")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Rover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String missionId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoverStatus status;

    private int batteryPercent;
    private double positionX;
    private double positionY;
    private Instant lastContactAt;

    @OneToMany(mappedBy = "rover", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        commands.add(command);
        command.setRover(this);
    }
}