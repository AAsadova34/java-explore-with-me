package ru.practicum.explore.ewm.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class CategoryDto {
    private Integer id;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    private String name;
}
