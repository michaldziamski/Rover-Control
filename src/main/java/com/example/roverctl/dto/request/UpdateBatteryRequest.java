package com.example.roverctl.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record UpdateBatteryRequest(@Min(0) @Max(100) int batteryPercent) {
}