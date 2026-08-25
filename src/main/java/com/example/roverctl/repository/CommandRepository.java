package com.example.roverctl.repository;

import com.example.roverctl.model.Command;
import com.example.roverctl.model.CommandStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;

public interface CommandRepository extends JpaRepository<Command, Long> {

    @EntityGraph(attributePaths = "rover")
    Page<Command> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "rover")
    Page<Command> findByRoverName(
            String name,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "rover")
    Page<Command> findByStatus(
            CommandStatus status,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "rover")
    Page<Command> findByRoverNameAndStatus(
            String name,
            CommandStatus status,
            Pageable pageable
    );

    long countByRoverName(String name);

    List<Command> findByMarsArrivalAtAfter(Instant now);

    List<Command> findByStatusOrderByEarthSentAtDesc(CommandStatus status);

    @Query("""
            SELECT c
            FROM Command c
            JOIN FETCH c.rover
            """)
    List<Command> findAllWithRover();

    @Query("""
            SELECT c
            FROM Command c
            JOIN FETCH c.rover
            WHERE c.rover.name = :name
            """)
    List<Command> findByRoverNameWithRover(String name);

    @Query("""
            SELECT c
            FROM Command c
            JOIN FETCH c.rover
            WHERE c.status = :status
            """)
    List<Command> findByStatusWithRover(CommandStatus status);

    @Query("""
            SELECT c
            FROM Command c
            JOIN FETCH c.rover
            WHERE c.rover.name = :name
              AND c.status = :status
            """)
    List<Command> findByRoverNameAndStatusWithRover(
            String name,
            CommandStatus status
    );

    @Query("""
            SELECT c
            FROM Command c
            JOIN FETCH c.rover
            WHERE c.rover.name = :name
              AND c.marsArrivalAt > :now
            """)
    List<Command> findPendingByRoverName(
            String name,
            Instant now
    );
}