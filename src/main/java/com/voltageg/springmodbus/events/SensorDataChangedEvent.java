package com.voltageg.springmodbus.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class SensorDataChangedEvent extends ApplicationEvent {
    private final int temperature;
    private final int humidity;
    private final LocalDateTime timeStamp;

    public SensorDataChangedEvent(Object source, int temperature, int humidity) {
        super(source);
        this.temperature = temperature;
        this.humidity = humidity;
        this.timeStamp = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }
}