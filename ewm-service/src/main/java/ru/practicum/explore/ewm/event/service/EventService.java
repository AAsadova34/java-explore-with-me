package ru.practicum.explore.ewm.event.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.dto.*;

import java.util.List;
import java.util.Set;

public interface EventService {

    @Transactional
    List<EventShortDto> getEventsByUser(int userId, int from, int size);

    @Transactional
    EventFullDto addEventByUser(int userId, NewEventDto newEventDto);

    @Transactional
    EventFullDto getEventByUser(int userId, int eventId);

    @Transactional
    EventFullDto updateEventByUser(int userId, int eventId, UpdateEventRequest updateEventRequest);

    @Transactional
    List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories, String rangeStart,
                                        String rangeEnd, int from, int size);

    @Transactional
    EventFullDto updateEventByAdmin(int eventId, UpdateEventRequest updateEventRequest);

    @Transactional
    EventFullDto getEventByPublic(int id);

    @Transactional
    List<EventShortDto> getEventsByPublic(String text, Integer[] categories, Boolean paid, String rangeStart,
                                          String rangeEnd, Boolean onlyAvailable, String sort, int from, int size);

    @Transactional
    Event getEventById(int eventId);

    @Transactional
    Event getEventByIdAndInitiatorId(int userId, int eventId);

    @Transactional
    List<Event> findEventsByIdIn(Set<Integer> ids);

    @Transactional
    List<EventShortDto> toListEventShortDto(List<Event> events);
}
