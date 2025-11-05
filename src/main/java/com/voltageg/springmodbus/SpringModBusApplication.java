package com.voltageg.springmodbus;

import com.voltageg.modbus.threads.MasterThread;
import com.voltageg.springmodbus.events.Event;
import com.voltageg.springmodbus.events.EventRepository;
import com.voltageg.springmodbus.events.Type;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.voltageg.springmodbus",
        "com.voltageg.modbus.threads"
})
public class SpringModBusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringModBusApplication.class, args);
    }

//    @Bean
//    public CommandLineRunner startSlaveThread(SlaveThread slaveThread) {
//        return args -> {
//            System.out.println("Starting SlaveThread...");
//            slaveThread.start();
//        };
//    }
    @Bean
    public CommandLineRunner startMasterThread(MasterThread masterThread) {
        return args -> {
            System.out.println("Starting MasterThread...");
            masterThread.start();
        };
    }


    @Bean
    public CommandLineRunner demo(EventRepository eventRepository) {
        return (args) -> {
            // Создаем тестовые данные
            eventRepository.save(new Event("App Initialised", Type.INFO));
            //eventRepository.save(new Event("A follow up event", java.time.LocalDateTime.now()));

            // Выводим в консоль для проверки
            //eventRepository.findAll().forEach(System.out::println);
        };
    }

    @Bean
    public CommandLineRunner init(EventRepository eventRepository) {
        return args -> {
            System.out.println("=== Database Management Commands ===");
            System.out.println("Database file: ./data/mydatabase.mv.db");
            System.out.println("Web interface: http://localhost:8080/h2-console");
            System.out.println("JDBC URL: jdbc:h2:file:./data/mydatabase");
            System.out.println("Username: sa, Password: (empty)");
            System.out.println("=== Application Started ===");

            // Создаем начальное событие
            if (eventRepository.count() == 0) {
                eventRepository.save(new Event("Application started with file database",
                        Type.INFO));
            }
        };
    }
}
