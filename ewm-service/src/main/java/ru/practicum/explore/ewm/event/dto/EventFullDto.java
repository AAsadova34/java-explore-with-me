package ru.practicum.explore.ewm.event.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.practicum.explore.ewm.category.CategoryDto;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.location.Location;
import ru.practicum.explore.ewm.user.dto.UserShortDto;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class EventFullDto {
    private Integer id;

    private String annotation;

    private CategoryDto category;

    private Integer confirmedRequests;

    private LocalDateTime createdOn;

    private String description;

    private LocalDateTime eventDate;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private LocalDateTime publishedOn;

    private Boolean requestModeration;

    private EventState state;

    private String title;

    private Integer views;
}
