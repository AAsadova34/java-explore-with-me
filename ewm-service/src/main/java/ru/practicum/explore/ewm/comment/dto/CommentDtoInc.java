package ru.practicum.explore.ewm.comment.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Accessors(chain = true)
public class CommentDtoInc {
    @Size(min = 2, max = 1000)
    @NotBlank(message = "Field: text. Error: must not be blank.")
    private String text;
}
