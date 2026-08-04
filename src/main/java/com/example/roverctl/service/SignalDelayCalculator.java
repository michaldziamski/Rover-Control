package com.example.roverctl.service;

import java.time.Duration;

public interface SignalDelayCalculator {

    Duration oneWayDelay();

    default Duration roundTripDelay() {
        return oneWayDelay().multipliedBy(2);
    }
}