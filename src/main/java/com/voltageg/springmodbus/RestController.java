package com.voltageg.springmodbus;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.voltageg.springmodbus.events.Event;
import com.voltageg.springmodbus.events.EventRepository;
import com.voltageg.springmodbus.events.Type;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.web.bind.annotation.RestController
public class RestController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    private final EventRepository eventRepository;

    // Внедрение зависимости через конструктор
    public RestController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping("/greeting")
    public Greeting greeting(@RequestParam(defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }


    @GetMapping("/events")
    public String getEvents(@RequestParam(required = false) String sort) {
        @SuppressWarnings("SwitchStatementWithTooFewBranches")
        List<Event> events = switch (sort != null ? sort : "date") {
            case "type" -> eventRepository.findAllByOrderByTypeAsc();
            default -> eventRepository.findAllByOrderByDateDesc();
        };

        return buildEventsPage(events, sort);
    }

    @GetMapping("/events/{type}")
    public String getEventsByType(@PathVariable String type) {
        try {
            Type eventType = Type.valueOf(type.toUpperCase());
            List<Event> events = eventRepository.findByTypeOrderByDateDesc(eventType);
            return buildEventsPage(events, "type");
        } catch (IllegalArgumentException e) {
            return "Invalid event type: " + type;
        }
    }

    private String buildEventsPage(List<Event> events, String currentSort) {
        StringBuilder builder = new StringBuilder();
        builder.append("""
                 <!DOCTYPE html>
                 <html>
                 <head>
                     <title>Events</title>
                     <meta http-equiv="refresh" content="10">
                     <style>
                         body { font-family: Arial, sans-serif; margin: 20px; }
                         .event { padding: 10px; margin: 5px 0; border-left: 4px solid; }
                         .timestamp { color: #666; font-size: 0.9em; }
                         .type { font-weight: bold; margin-right: 10px; }
                         .filters { margin: 20px 0; padding: 10px; background: #f5f5f5; }
                         .filter-btn { margin: 0 5px; padding: 5px 10px; cursor: pointer; }
                         .active { background: #007bff; color: white; }
                \s
                         /* Цвета для разных типов событий */
                         .SENSOR_DATA_UPDATE { border-color: #28a745; background: #f8fff9; }
                         .HIGH_LEVEL_ALARM { border-color: #dc3545; background: #fff5f5; }
                         .LOW_LEVEL_ALARM { border-color: #ffc107; background: #fffdf5; }
                         .INFO { border-color: #17a2b8; background: #f8fdff; }
                     </style>
                 </head>
                 <body>
                     <h1>Events List</h1>
                \s
                     <div class="filters">
                         <strong>Sort by:</strong>
                         <a href="/events?sort=date" class="filter-btn\s""")
                .append("date".equals(currentSort) ? "active" : "")
                .append("""
                <a href="/events?sort=type" class="filter-btn\s""")
                .append("type".equals(currentSort) ? "active" : "")
                .append("""
                    >Type</a>
                
                    <strong style="margin-left: 20px;">Filter by type:</strong>
                    <a href="/events/SENSOR_DATA_UPDATE" class="filter-btn">Sensor Updates</a>
                    <a href="/events/HIGH_LEVEL_ALARM" class="filter-btn">High Alarms</a>
                    <a href="/events/LOW_LEVEL_ALARM" class="filter-btn">Low Alarms</a>
                    <a href="/events/INFO" class="filter-btn">Info</a>
                    <a href="/events" class="filter-btn">All Events</a>
                </div>
                
                <div id="events-container">
                
                """);

        if (events.isEmpty()) {
            builder.append("<p>No events found</p>");
        } else {
            events.forEach(event -> {
                String typeClass = event.getType().name();
                String typeLabel = event.getType().name().replace("_", " ")
                        .toLowerCase().transform(s -> {
                            char[] charArray = s.toCharArray();
                            for (int i = 0; i < charArray.length; i++) {
                                char c = charArray[i];
                                if (i == 0) {
                                    charArray[i] = String.valueOf(c).toUpperCase().charAt(0);
                                    continue;
                                }
                                char c1 = charArray[i-1];
                                if (c1 == ' ') charArray[i] = String.valueOf(c).toUpperCase().charAt(0);
                            }
                            return String.valueOf(charArray);
                        });

                builder.append("<div class='event ").append(typeClass).append("'>")
                        .append("<span class='type'>").append(typeLabel).append("</span>")
                        .append("<div class='timestamp'>").append(event.getDate()).append("</div>")
                        .append("<div>").append(event.getTitle()).append("</div>")
                        .append("</div>");
            });
        }

        builder.append("""
                    </div>
                
                    <script>
                        // Авто-обновление каждые 10 секунд
                        setTimeout(() => {
                            window.location.reload();
                        }, 10000);
                    </script>
                </body>
                </html>
                """);

        return builder.toString();
    }
}