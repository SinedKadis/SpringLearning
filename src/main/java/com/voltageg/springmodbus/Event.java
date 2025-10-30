package com.voltageg.springmodbus;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(name = "eventDate")
    private LocalDateTime date;
}
