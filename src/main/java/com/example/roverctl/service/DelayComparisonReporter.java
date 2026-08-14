package com.example.roverctl.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class DelayComparisonReporter {

    private final List<SignalDelayCalculator> calculators;

    public DelayComparisonReporter(List<SignalDelayCalculator> calculators) {
        this.calculators = calculators;
    }

    public void logComparison() {
        log.info("--- Delay comparison ---");

        calculators.forEach(calculator -> {
            try {
                log.info("{}: {} minutes",
                        calculator.getClass().getSimpleName(),
                        calculator.oneWayDelay().toMinutes());
            } catch (RuntimeException e) {
                log.warn("{}: communication unavailable - {}",
                        calculator.getClass().getSimpleName(),
                        e.getMessage());
            }
        });
    }
}