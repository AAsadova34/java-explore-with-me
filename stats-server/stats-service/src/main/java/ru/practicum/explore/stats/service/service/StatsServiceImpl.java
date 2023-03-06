package ru.practicum.explore.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.stats.service.Stats;
import ru.practicum.explore.stats.dto.StatsInnerDto;
import ru.practicum.explore.stats.dto.StatsOuterDto;
import ru.practicum.explore.stats.service.StatsRepository;

import java.time.LocalDateTime;
import java.util.*;

import static ru.practicum.explore.stats.service.Logger.logStorageChanges;
import static ru.practicum.explore.stats.service.Mapper.toStats;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public void addStats(StatsInnerDto statsInnerDto) {
        Stats stats = toStats(statsInnerDto);
        Stats statsStorage = statsRepository.save(stats);
        logStorageChanges("Add stats", statsStorage.toString());
    }

    @Override
    public List<StatsOuterDto> getStats(LocalDateTime start, LocalDateTime end, String[] uris, boolean unique) {
        List<StatsOuterDto> options;
        if (uris != null && uris.length != 0) {
            Set<String> setUris = Set.of(uris);
            if (unique) {
                options = statsRepository.findByUriAndUniqueId(setUris, start, end);
            } else {
                options = statsRepository.findByUriAndNotUniqueId(setUris, start, end);
            }
        } else {
            if (unique) {
                options = statsRepository.findAllUniqueId(start, end);
            } else {
                options = statsRepository.findAllNotUniqueId(start, end);
            }
        }
        return options;
    }
}
