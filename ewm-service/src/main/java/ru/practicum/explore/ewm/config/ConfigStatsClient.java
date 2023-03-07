package ru.practicum.explore.ewm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.explore.stats.client.StatsClient;

@Configuration
public class ConfigStatsClient {
    @Value("${stats-server.url}")
    private String url;

    @Bean
    public StatsClient statsClient() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        return new StatsClient(url, builder);
    }
}

