package ru.practicum.explore.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatsOuterDto {
    private String app;
    private String uri;
    private int hits;
}
