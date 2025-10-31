package com.voltageg.springmodbus;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import com.voltageg.springmodbus.events.EventRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
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


//    @GetMapping("/events")
//    public String getEvents() {
//        StringBuilder builder = new StringBuilder();
//        eventRepository.findAll().forEach(event ->
//                builder.append("Event (")
//                        .append(event.getDate())
//                        .append(") : ")
//                        .append(event.getTitle())
//                        .append("<br>")
//        );
//        return builder.toString();
//    }
@GetMapping("/events")
public Object getEvents(HttpServletRequest request) {
    String acceptHeader = request.getHeader("Accept");

    if (acceptHeader != null && acceptHeader.contains("application/json")) {
        // Возвращаем JSON для API клиентов
        return eventRepository.findAll().stream()
                .map(event -> Map.of(
                        "id", event.getId(),
                        "title", event.getTitle(),
                        "date", event.getDate()
                ))
                .collect(Collectors.toList());
    } else {
        // Возвращаем HTML для браузеров
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><h1>Events</h1>");

        eventRepository.findAll().forEach(event ->
                builder.append("<div style='margin: 10px 0; padding: 10px; border: 1px solid #ccc;'>")
                        .append("<strong>Event (")
                        .append(event.getDate().toString().split("T")[0])
                        .append("---")
                        .append(event.getDate().toString().split("T")[1])
                        .append(")</strong>: ")
                        .append(event.getTitle())
                        .append("</div>")
        );

        builder.append("</body></html>");
        return builder.toString();
    }
}
}