package com.voltageg.springmodbus.events;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "Events")
@Getter
@Setter
@NoArgsConstructor
public class Event implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "eventType")
    private Type type;

    public Event(String title, Type type) {
        this(title, type, LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    public Event(String title, Type type, LocalDateTime date) {
        this.title = title;
        this.type = type;
        this.date = date;
    }

    @Column(name = "eventDate")
    private LocalDateTime date;
}
