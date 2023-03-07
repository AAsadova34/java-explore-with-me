package ru.practicum.explore.ewm.event.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.explore.ewm.category.CategoryDto;
import ru.practicum.explore.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EventShortDto {
    private Integer id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Boolean paid;

    private String title;

    private Integer views;
}
