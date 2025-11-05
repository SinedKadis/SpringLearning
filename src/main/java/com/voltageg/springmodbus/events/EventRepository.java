package com.voltageg.springmodbus.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Метод для получения событий, отсортированных по типу
    List<Event> findAllByOrderByTypeAsc();

    // Метод для получения событий определенного типа
    List<Event> findByTypeOrderByDateDesc(Type type);

    // Метод для получения всех событий с сортировкой по дате (по убыванию)
    List<Event> findAllByOrderByDateDesc();
}
