package com.voltageg.springmodbus.events;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SensorDataChangedEventListener {

    private final EventRepository eventRepository;

    public SensorDataChangedEventListener(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @EventListener
    @Transactional
    public void handleSensorDataChanged(SensorDataChangedEvent event) {
        String eventMessage = String.format(
                "Sensor data changed: Temperature=%d°C, Humidity=%d%%",
                event.getTemperature(),
                event.getHumidity()
        );

        Event dbEvent = new Event(eventMessage, event.getTimeStamp());
        eventRepository.save(dbEvent);

        System.out.println("Event saved: " + eventMessage);
    }
}
