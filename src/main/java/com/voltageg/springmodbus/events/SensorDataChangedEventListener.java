package com.voltageg.springmodbus.events;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class SensorDataChangedEventListener {

    public static final int TEMPERATURE_TOP_LIMIT = 30;
    public static final int HUMIDITY_TOP_LIMIT = 60;
    private static final int TEMPERATURE_LOW_LIMIT = 0;
    private static final int HUMIDITY_LOW_LIMIT = 30;
    private final EventRepository eventRepository;
    private final SensorEventService sensorEventService;

    public SensorDataChangedEventListener(EventRepository eventRepository,SensorEventService sensorEventService) {
        this.eventRepository = eventRepository;
        this.sensorEventService = sensorEventService;
    }

    @EventListener
    public void handleSensorDataChanged(SensorDataChangedEvent event) {
        int temperature = event.getTemperature();
        int humidity = event.getHumidity();
        Type type = event.getType();
        if (type.equals(Type.SENSOR_DATA_UPDATE)){
            handleSensorUpdate(event, temperature, humidity);
        }
        if (type.equals(Type.HIGH_LEVEL_ALARM)){
            handleHighLevel(event, temperature, humidity);
        }
        if (type.equals(Type.LOW_LEVEL_ALARM)) {
            handleLowLevel(event, temperature, humidity);
        }
    }

    private void handleLowLevel(SensorDataChangedEvent event, int temperature, int humidity) {
        if (temperature < TEMPERATURE_LOW_LIMIT) {
            String eventMessage1 = String.format(
                    "Temperature below limit: Temperature=%d°C, Allowed: >%d",
                    temperature,
                    TEMPERATURE_LOW_LIMIT
            );
            Event dbEvent1 = new Event(eventMessage1, Type.LOW_LEVEL_ALARM, event.getTimeStamp());
            eventRepository.save(dbEvent1);

            System.out.println("Event saved: " + eventMessage1);
        }
        if (humidity < HUMIDITY_LOW_LIMIT) {
            String eventMessage1 = String.format(
                    "Humidity below limit: Humidity=%d%%, Allowed: >%d",
                    humidity,
                    HUMIDITY_LOW_LIMIT
            );
            Event dbEvent1 = new Event(eventMessage1, Type.LOW_LEVEL_ALARM, event.getTimeStamp());
            eventRepository.save(dbEvent1);

            System.out.println("Event saved: " + eventMessage1);
        }
    }

    private void handleHighLevel(SensorDataChangedEvent event, int temperature, int humidity) {
        if (temperature > TEMPERATURE_TOP_LIMIT) {
            String eventMessage1 = String.format(
                    "Temperature over limit: Temperature=%d°C, Allowed: <%d",
                    temperature,
                    TEMPERATURE_TOP_LIMIT
            );
            Event dbEvent1 = new Event(eventMessage1, Type.HIGH_LEVEL_ALARM, event.getTimeStamp());
            eventRepository.save(dbEvent1);

            System.out.println("Event saved: " + eventMessage1);
        }
        if (humidity > HUMIDITY_TOP_LIMIT) {
            String eventMessage1 = String.format(
                    "Humidity over limit: Humidity=%d%%, Allowed: <%d",
                    humidity,
                    HUMIDITY_TOP_LIMIT
            );
            Event dbEvent1 = new Event(eventMessage1, Type.HIGH_LEVEL_ALARM, event.getTimeStamp());
            eventRepository.save(dbEvent1);

            System.out.println("Event saved: " + eventMessage1);
        }
    }

    private void handleSensorUpdate(SensorDataChangedEvent event, int temperature, int humidity) {
        String eventMessage = String.format(
                "Sensor data changed: Temperature=%d°C, Humidity=%d%%",
                temperature,
                humidity
        );

        Event dbEvent = new Event(eventMessage, Type.SENSOR_DATA_UPDATE, event.getTimeStamp());
        eventRepository.save(dbEvent);

        System.out.println("Event saved: " + eventMessage);

        if (temperature > TEMPERATURE_TOP_LIMIT || humidity > HUMIDITY_TOP_LIMIT)
            sensorEventService.publishSensorChange(temperature, humidity,Type.HIGH_LEVEL_ALARM);
        if (temperature < TEMPERATURE_LOW_LIMIT || humidity < HUMIDITY_LOW_LIMIT)
            sensorEventService.publishSensorChange(temperature, humidity,Type.LOW_LEVEL_ALARM);
    }
}
