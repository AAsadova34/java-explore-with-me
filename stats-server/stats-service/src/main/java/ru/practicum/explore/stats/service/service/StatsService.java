package ru.practicum.explore.stats.service.service;

import ru.practicum.explore.stats.dto.StatsInnerDto;
import ru.practicum.explore.stats.dto.StatsOuterDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addStats(StatsInnerDto statsInnerDto);

    List<StatsOuterDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
