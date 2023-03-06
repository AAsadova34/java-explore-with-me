package ru.practicum.explore.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UpdateCompilationDto {
    private List<Integer> events;

    private Boolean pinned;
}
