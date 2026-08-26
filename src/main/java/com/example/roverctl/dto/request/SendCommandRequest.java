package com.example.roverctl.dto.request;

import com.example.roverctl.model.CommandType;
import jakarta.validation.constraints.NotNull;

public record SendCommandRequest(@NotNull CommandType type) {
}