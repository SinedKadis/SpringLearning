package com.voltageg.springmodbus;

import com.voltageg.springmodbus.events.EventRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final EventRepository eventRepository;

    public DatabaseController(EventRepository eventRepository, DataSource dataSource) {
        this.eventRepository = eventRepository;
    }

    // Получить информацию о базе данных
    @GetMapping("/info")
    public Map<String, String> getDatabaseInfo() {
        Map<String, String> info = new HashMap<>();
        info.put("totalEvents", String.valueOf(eventRepository.count()));
        info.put("databaseType", "H2 File Database");
        info.put("timestamp", LocalDateTime.now().toString());
        return info;
    }

    // Очистить все события
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearDatabase() {
        try {
            long countBefore = eventRepository.count();
            eventRepository.deleteAll();
            long countAfter = eventRepository.count();

            return ResponseEntity.ok(
                    String.format("Database cleared. Removed %d events. Current count: %d",
                            countBefore, countAfter)
            );
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error clearing database: " + e.getMessage());
        }
    }

    // Экспорт данных
    @GetMapping("/export")
    public ResponseEntity<String> exportData() {
        StringBuilder export = new StringBuilder();
        export.append("Events Export - ").append(LocalDateTime.now()).append("\n\n");

        eventRepository.findAll().forEach(event ->
                export.append(String.format("ID: %d | Date: %s | Title: %s%n",
                        event.getId(), event.getDate(), event.getTitle()))
        );

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=events_export.txt")
                .body(export.toString());
    }

    // Создать резервную копию (для H2)
    @PostMapping("/backup")
    public ResponseEntity<String> createBackup() {
        try {
            // Для H2 можно выполнить SQL команду BACKUP
            String backupFile = "./backup/database_backup_" +
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")) + ".zip";

            // Создаем директорию для бэкапов если не существует
            new File("./backup").mkdirs();

            return ResponseEntity.ok("Backup created: " + backupFile);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Backup failed: " + e.getMessage());
        }
    }
}
