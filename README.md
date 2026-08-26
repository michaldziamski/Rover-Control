[README-Rover-Control-EN.md](https://github.com/user-attachments/files/31486517/README-Rover-Control-EN.md)
# Rover Control

A Mars rover command-and-control system built with Spring Boot. It simulates realistic Earth–Mars signal delay, manages a command queue, and tracks rover telemetry throughout a mission.

## About the project

Rover Control is a backend REST API that models the work of a Mars mission control center:

- every command sent to a rover has a real **delayed arrival time** (the signal takes X minutes to travel from Earth to Mars before the rover "receives" it),
- rovers have a battery level, position, and status (`OPERATIONAL`, `DEGRADED`, `HIBERNATING`, `LOST`),
- the system enforces mission rules — e.g. a maximum number of queued commands per rover, or a minimum battery level required to drill,
- rovers report telemetry that can later be read back through the API.

## Features

- **Rover management** — list rovers, view details, update battery level
- **Sending commands** — `DRIVE`, `TAKE_PHOTO`, `DRILL`, `ADJUST_ANTENNA`, `WAKE`, `HIBERNATE`
- **Signal delay simulation** — calculates when a command actually arrives on Mars (including handling periodic communication blackouts, e.g. solar conjunction)
- **Command history and queue** — view completed and pending commands, cancel a command
- **Emergency mode** — an `emergency-hibernate` command for immediately putting a rover to sleep
- **Telemetry** — receiving and reading telemetry packets from a rover
- **Mission status** — an overview of the whole mission's state
- **Business rule validation** — e.g. rejecting commands when battery is too low, the queue limit is exceeded, or communication is down

## Tech stack

- **Java** + **Spring Boot** (Web, Data JPA, Validation)
- **H2** — embedded file-based database (data stored in `data/rovers.mv.db`), with an H2 console for inspecting data
- **Lombok** — reduces boilerplate in entities and DTOs
- **Maven** (Maven Wrapper) — build tool
- **JUnit** — unit and integration tests (including transactional rollback tests for commands)

## Architecture

The project is organized in layers:

```
controller/   - REST endpoints (Rover, Command, Telemetry, Mission Status)
service/      - business logic, including signal delay calculators
model/        - JPA entities (Rover, Command, TelemetryPacket) + status enums
repository/   - data access layer (Spring Data JPA)
dto/          - request/response objects kept separate from entities
exception/    - dedicated domain exceptions + a global exception handler
config/       - mission configuration (parameters from application.properties) and clock config
runner/       - a demo runner that plays out a sample mission scenario on startup
```

Worth noting: `ClockConfig` injects the system clock as a bean, which makes time-dependent logic (signal delays, command expiry) easy to test instead of scattering `Instant.now()` calls throughout the code.

## Main API endpoints

Base prefix: `/api/v1`

| Method | Endpoint | Description |
|---|---|---|
| GET | `/rovers` | List all rovers |
| GET | `/rovers/{name}` | Rover details |
| PATCH | `/rovers/{name}/battery` | Update battery level |
| POST | `/rovers/{name}/commands` | Send a new command to a rover |
| GET | `/rovers/{name}/commands` | Command history for a rover |
| GET | `/rovers/{name}/commands/pending` | Pending commands |
| POST | `/rovers/{name}/emergency-hibernate` | Emergency-hibernate a rover |
| POST | `/rovers/{name}/telemetry` | Submit a telemetry packet |
| GET | `/rovers/{name}/telemetry` | Read a rover's telemetry |
| GET | `/commands` | List commands (paginated) |
| GET | `/commands/{id}` | Command details |
| DELETE | `/commands/{id}` | Cancel a command |
| GET | `/mission/status` | Overall mission status |

Sample requests are available in the `request.http` file.

## Mission configuration

Mission parameters are set in `application.properties` and injected via `MissionProperties`:

```properties
mission.name=Mars Exploration Program
mission.min-signal-delay-minutes=4
mission.max-signal-delay-minutes=24
mission.low-battery-threshold=30
mission.drill-battery-requirement=50
mission.max-commands-per-rover=5
```

This makes it easy to tune signal delay, battery thresholds, and queue limits without touching the code.

## Running locally

Requires a JDK compatible with Spring Boot 4 (Java 21+).

```bash
# clone the repository
git clone https://github.com/michaldziamski/Rover-Control.git
cd Rover-Control

# run the application (Maven Wrapper)
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080` by default. The H2 console is available at `/h2-console` (JDBC URL: `jdbc:h2:file:./data/rovers`).

### Tests

```bash
./mvnw test
```
---

Built as a learning project for Spring Boot and for designing systems with time-based/domain logic.
