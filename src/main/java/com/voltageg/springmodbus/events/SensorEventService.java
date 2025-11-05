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
        publishSensorChange(temperature, humidity, Type.SENSOR_DATA_UPDATE);
    }

    public void publishSensorChange(int temperature, int humidity, Type type) {
        SensorDataChangedEvent event = new SensorDataChangedEvent(this, temperature, humidity,type);
        eventPublisher.publishEvent(event);
    }
}
