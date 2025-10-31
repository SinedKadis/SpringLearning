package com.voltageg.springmodbus.events;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @Column(name = "eventDate")
    private LocalDateTime date;

    public Event(String title, LocalDateTime date) {
        this.title = title;
        this.date = date;
    }
}
