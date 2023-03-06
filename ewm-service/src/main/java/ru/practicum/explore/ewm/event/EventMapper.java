package ru.practicum.explore.ewm.event;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.category.Category;
import ru.practicum.explore.ewm.event.dto.EventFullDto;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.event.dto.NewEventDto;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.location.Location;
import ru.practicum.explore.ewm.exception.ValidationException;
import ru.practicum.explore.ewm.user.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static ru.practicum.explore.ewm.category.CategoryMapper.toCategoryDto;
import static ru.practicum.explore.ewm.user.UserMapper.toUserShortDto;

@UtilityClass
public class EventMapper {

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto, Category category,
                                User initiator, Location location, EventState state) {
        return new Event()
                .setAnnotation(newEventDto.getAnnotation())
                .setCategory(category)
                .setDescription(newEventDto.getDescription())
                .setEventDate(newEventDto.getEventDate())
                .setInitiator(initiator)
                .setLocation(location)
                .setPaid(newEventDto.getPaid())
                .setParticipantLimit(newEventDto.getParticipantLimit())
                .setRequestModeration(newEventDto.getRequestModeration())
                .setTitle(newEventDto.getTitle())
                .setState(state);
    }

    public static EventFullDto toEventFullDto(Event event, long numberConfirmedRequests) {
        return new EventFullDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(toCategoryDto(event.getCategory()))
                .setConfirmedRequests((int) numberConfirmedRequests)
                .setCreatedOn(event.getCreated())
                .setDescription(event.getDescription())
                .setEventDate(event.getEventDate())
                .setInitiator(toUserShortDto(event.getInitiator()))
                .setLocation(event.getLocation())
                .setPaid(event.getPaid())
                .setParticipantLimit(event.getParticipantLimit())
                .setPublishedOn(event.getPublished())
                .setRequestModeration(event.getRequestModeration())
                .setState(event.getState())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public static EventShortDto toEventShortDto(Event event, long numberConfirmedRequests) {
        return new EventShortDto()
                .setId(event.getId())
                .setAnnotation(event.getAnnotation())
                .setCategory(toCategoryDto(event.getCategory()))
                .setConfirmedRequests((int) numberConfirmedRequests)
                .setEventDate(event.getEventDate())
                .setInitiator(toUserShortDto(event.getInitiator()))
                .setPaid(event.getPaid())
                .setTitle(event.getTitle())
                .setViews(event.getViews());
    }

    public static LocalDateTime toLocalDateTime(String strDataTime) {
        LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(strDataTime, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ValidationException(e.getMessage());
        }
        return localDateTime;
    }

}
