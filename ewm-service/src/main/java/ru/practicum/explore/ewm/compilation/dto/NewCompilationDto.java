package ru.practicum.explore.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private List<Integer> events;

    private Boolean pinned;

    @NotNull
    @NotBlank
    private String title;
}
