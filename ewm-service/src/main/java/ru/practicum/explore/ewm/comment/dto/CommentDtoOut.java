package ru.practicum.explore.ewm.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class CommentDtoOut {
    private Integer id;

    private EventShortDto event;

    private UserShortDto user;

    private String text;

    private Boolean redaction;

    private LocalDateTime created;
}
