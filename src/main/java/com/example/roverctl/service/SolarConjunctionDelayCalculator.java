package com.example.roverctl.service;

import com.example.roverctl.exception.CommunicationBlackoutException;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component("solarConjunctionDelayCalculator")
public class SolarConjunctionDelayCalculator implements SignalDelayCalculator {

    @Override
    public Duration oneWayDelay() {
        throw new CommunicationBlackoutException(
                "Communication blackout: Sun is between Earth and Mars");
    }

    @Override
    public Duration roundTripDelay() {
        throw new CommunicationBlackoutException(
                "Communication blackout: Sun is between Earth and Mars");
    }
}