package ru.practicum.explore.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.Stats;
import ru.practicum.explore.StatsInnerDto;
import ru.practicum.explore.StatsOuterDto;
import ru.practicum.explore.StatsRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.practicum.explore.Logger.logStorageChanges;
import static ru.practicum.explore.Mapper.toLocalDateTime;
import static ru.practicum.explore.Mapper.toStats;

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
    public List<StatsOuterDto> getStats(String start, String end, String[] uris, boolean unique) {
        List<StatsOuterDto> options = new ArrayList<>();
        if (uris == null || uris.length == 0) {
            if (unique) {
                options = statsRepository.findAllUniqueId(toLocalDateTime(start), toLocalDateTime(end));
            } else {
                statsRepository.findAllNotUniqueId(toLocalDateTime(start), toLocalDateTime(end));
            }
        } else {
            for (String uri : uris) {
                Optional<StatsOuterDto> stat;
                if (unique) {
                    stat = statsRepository.findByUriAndUniqueId(uri,
                            toLocalDateTime(start), toLocalDateTime(end));
                } else {
                    stat = statsRepository.findByUriAndNotUniqueId(uri,
                            toLocalDateTime(start), toLocalDateTime(end));
                }
                if (stat.isPresent()) {
                    options.add(stat.get());
                }
            }
            if (!options.isEmpty()) {
                options = options.stream()
                        .sorted(Comparator.comparing(StatsOuterDto::getHits).reversed())
                        .collect(Collectors.toList());
            }
        }
        return options;
    }
}
