package ru.practicum.explore.stats.service;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.stats.dto.StatsInnerDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Mapper {
    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Stats toStats(StatsInnerDto statsInnerDto) {
        Stats stats = new Stats();
        stats.setApp(statsInnerDto.getApp());
        stats.setUri(statsInnerDto.getUri());
        stats.setIp(statsInnerDto.getIp());
        stats.setTimestamp(toLocalDateTime(statsInnerDto.getTimestamp()));
        return stats;
    }

    public static LocalDateTime toLocalDateTime(String strDataTime) {
        return LocalDateTime.parse(strDataTime, format);
    }
}
