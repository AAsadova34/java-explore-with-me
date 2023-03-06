package ru.practicum.explore.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.explore.stats.client.StatsClient;

@SpringBootApplication(scanBasePackageClasses = {EwmApp.class, StatsClient.class })
public class EwmApp {

    public static void main(String[] args) {
        SpringApplication.run(EwmApp.class, args);
    }
}
