package com.example.roverctl.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SolarConjunctionMonitor {

    private final SignalDelayCalculator delayCalculator;

    public SolarConjunctionMonitor(
            @Qualifier("solarConjunctionDelayCalculator")
            SignalDelayCalculator delayCalculator) {
        this.delayCalculator = delayCalculator;
    }

    public void checkCommunication() {
        delayCalculator.oneWayDelay();
    }
}