package com.voltageg.springmodbus.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

@Getter
public class SensorDataChangedEvent extends ApplicationEvent {
    private final int temperature;
    private final int humidity;
    private final LocalDateTime timeStamp;
    private final Type type;

    public SensorDataChangedEvent(Object source, int temperature, int humidity, Type type) {
        super(source);
        this.temperature = temperature;
        this.humidity = humidity;
        this.timeStamp = LocalDateTime.now();
        this.type = type;
    }
}