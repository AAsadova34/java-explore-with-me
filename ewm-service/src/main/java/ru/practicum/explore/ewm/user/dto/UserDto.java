package ru.practicum.explore.ewm.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserDto {

    private Integer id;

    @NotBlank(message = "Field: email. Error: must not be blank. Value: null")
    private String email;

    @NotBlank(message = "Field: name. Error: must not be blank. Value: null")
    private String name;
}
