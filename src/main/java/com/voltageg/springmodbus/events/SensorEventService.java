package com.voltageg.springmodbus.events;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SensorEventService {

    private final ApplicationEventPublisher eventPublisher;

    public SensorEventService(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void publishSensorChange(int temperature, int humidity) {
        SensorDataChangedEvent event = new SensorDataChangedEvent(this, temperature, humidity);
        eventPublisher.publishEvent(event);
    }
}
