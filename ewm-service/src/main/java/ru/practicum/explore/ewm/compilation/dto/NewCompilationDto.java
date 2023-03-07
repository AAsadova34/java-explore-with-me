package ru.practicum.explore.ewm.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
public class NewCompilationDto {
    private List<Integer> events;

    private Boolean pinned;

    @NotBlank(message = "Field: title. Error: must not be blank.")
    private String title;
}
