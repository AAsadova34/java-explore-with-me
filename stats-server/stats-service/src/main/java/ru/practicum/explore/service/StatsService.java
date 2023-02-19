package ru.practicum.explore.service;

import ru.practicum.explore.StatsInnerDto;
import ru.practicum.explore.StatsOuterDto;

import java.util.List;

public interface StatsService {
    void addStats(StatsInnerDto statsInnerDto);

    List<StatsOuterDto> getStats(String start, String end, String[] uris, boolean unique);
}
