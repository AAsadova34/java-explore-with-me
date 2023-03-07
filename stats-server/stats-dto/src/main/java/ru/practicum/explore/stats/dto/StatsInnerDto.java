package ru.practicum.explore.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@Accessors(chain = true)
public class StatsInnerDto {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
