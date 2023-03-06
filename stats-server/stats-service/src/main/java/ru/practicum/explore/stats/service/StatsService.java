package ru.practicum.explore.stats.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.stats.dto.StatsInnerDto;
import ru.practicum.explore.stats.dto.StatsOuterDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    @Transactional
    void addStats(StatsInnerDto statsInnerDto);

    @Transactional
    List<StatsOuterDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique);
}
