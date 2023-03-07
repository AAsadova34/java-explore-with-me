package ru.practicum.explore.ewm.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.category.Category;
import ru.practicum.explore.ewm.category.service.CategoryService;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.EventRepository;
import ru.practicum.explore.ewm.event.QEvent;
import ru.practicum.explore.ewm.event.dto.*;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.enums.StateAction;
import ru.practicum.explore.ewm.event.location.Location;
import ru.practicum.explore.ewm.event.location.LocationRepository;
import ru.practicum.explore.ewm.exception.ConflictException;
import ru.practicum.explore.ewm.exception.NotFoundException;
import ru.practicum.explore.ewm.participation_request.ParticipationRequestRepository;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;
import ru.practicum.explore.ewm.user.User;
import ru.practicum.explore.ewm.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static ru.practicum.explore.ewm.event.enums.EventSort.*;
import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;
import static ru.practicum.explore.ewm.event.EventMapper.*;
import static ru.practicum.explore.ewm.event.EventMapper.toEventFullDto;
import static ru.practicum.explore.ewm.event.enums.EventState.*;
import static ru.practicum.explore.ewm.event.enums.StateAction.*;
import static ru.practicum.explore.ewm.participation_request.enums.RequestStatus.CONFIRMED;
import static ru.practicum.explore.ewm.event.Predicate.*;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final LocationRepository locationRepository;
    private final ParticipationRequestRepository requestRepository;

    @Transactional
    @Override
    public List<EventShortDto> getEventsByUser(int userId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> events = eventRepository.findEventsByInitiatorId(userId, pageable);
        HashMap<Integer, Integer> confirmedRequests = getConfirmedRequests(events);

        return events.stream()
                .map(e -> toEventShortDto(e, confirmedRequests.get(e.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto addEventByUser(int userId, NewEventDto newEventDto) {
        checkEventDate(newEventDto.getEventDate(), 2);
        Category category = categoryService.getCategoryById(newEventDto.getCategory());
        User initiator = userService.getUserById(userId);
        Location location = saveLocation(newEventDto.getLocation());

        Event event = toEvent(newEventDto, category, initiator, location, EventState.PENDING);
        Event eventStorage = eventRepository.save(event);
        logStorageChanges("Add event", eventStorage.toString());

        int numberConfirmedRequests = requestRepository.getNumberRequests(eventStorage.getId(), CONFIRMED);
        return toEventFullDto(eventStorage, numberConfirmedRequests);
    }

    @Transactional
    @Override
    public EventFullDto getEventByUser(int userId, int eventId) {
        Event eventStorage = getEventByIdAndInitiatorId(eventId, userId);
        int numberConfirmedRequests = requestRepository.getNumberRequests(eventStorage.getId(), CONFIRMED);
        return toEventFullDto(eventStorage, numberConfirmedRequests);
    }

    @Transactional
    @Override
    public EventFullDto updateEventByUser(int userId, int eventId, UpdateEventRequest updateEvent) {
        Event oldEvent = getEventByIdAndInitiatorId(eventId, userId);
        checkEventDate(oldEvent.getEventDate(), 2);

        Event eventStorage = eventRepository.save(updateEvent(oldEvent, updateEvent));
        logStorageChanges("Update event", eventStorage.toString());

        int numberConfirmedRequests = requestRepository.getNumberRequests(eventStorage.getId(), CONFIRMED);
        return toEventFullDto(eventStorage, numberConfirmedRequests);
    }

    @Transactional
    @Override
    public List<EventFullDto> getEventsByAdmin(Integer[] users, String[] states, Integer[] categories,
                                               String rangeStart, String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        BooleanBuilder builder = new BooleanBuilder();
        getPredicateByUserId(users).ifPresent(builder::and);
        getPredicateByStates(states).ifPresent(builder::and);
        getPredicateByCategoryId(categories).ifPresent(builder::and);
        getPredicateByStartForAdmin(rangeStart).ifPresent(builder::and);
        getPredicateByEnd(rangeEnd).ifPresent(builder::and);

        List<Event> events = eventRepository.findAll(builder, pageable).getContent();
        HashMap<Integer, Integer> confirmedRequests = getConfirmedRequests(events);

        return events.stream()
                .map(e -> toEventFullDto(e, confirmedRequests.get(e.getId())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public EventFullDto updateEventByAdmin(int eventId, UpdateEventRequest updateEvent) {
        Event oldEvent = getEventById(eventId);
        checkEventDate(oldEvent.getEventDate(), 1);

        Event eventStorage = eventRepository.save(updateEvent(oldEvent, updateEvent));
        logStorageChanges("Update event", eventStorage.toString());

        int numberConfirmedRequests = requestRepository.getNumberRequests(eventStorage.getId(), CONFIRMED);
        return toEventFullDto(eventStorage, numberConfirmedRequests);
    }

    @Transactional
    @Override
    public EventFullDto getEventByPublic(int id) {
        Optional<Event> optEvent = eventRepository.getEventByIdAndState(id, PUBLISHED);
        if (optEvent.isEmpty()) {
            throw new NotFoundException(String.format("Event with id=%s was not found.", id));
        }
        Event event = optEvent.get();
        int numberConfirmedRequests = requestRepository.getNumberRequests(event.getId(), CONFIRMED);
        addView(List.of(event));
        return toEventFullDto(event, numberConfirmedRequests);
    }

    @Transactional
    @Override
    public List<EventShortDto> getEventsByPublic(String text, Integer[] categories, Boolean paid, String rangeStart,
                                                 String rangeEnd, Boolean onlyAvailable, String sort, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(QEvent.event.state.eq(PUBLISHED));
        getPredicateByText(text).ifPresent(builder::and);
        getPredicateByCategoryId(categories).ifPresent(builder::and);
        getPredicateByPaid(paid).ifPresent(builder::and);
        getPredicateByStartForPublic(rangeStart).ifPresent(builder::and);
        getPredicateByEnd(rangeEnd).ifPresent(builder::and);

        List<Event> events = eventRepository.findAll(builder, pageable).getContent();
        HashMap<Integer, Integer> confirmedRequests = getConfirmedRequests(events);

        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(e -> (e.getParticipantLimit() == 0
                            || e.getParticipantLimit() > confirmedRequests.get(e.getId())))
                    .collect(Collectors.toList());
        }

        addView(events);

        Optional<Comparator<EventShortDto>> comparator = getComparatorEventShortDto(sort);
        if (comparator.isPresent()) {
            return events.stream()
                    .map(e -> toEventShortDto(e, confirmedRequests.get(e.getId())))
                    .sorted(comparator.get())
                    .collect(Collectors.toList());
        } else {
            return events.stream()
                    .map(e -> toEventShortDto(e, confirmedRequests.get(e.getId())))
                    .collect(Collectors.toList());
        }
    }

    @Transactional
    @Override
    public Event getEventById(int eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NotFoundException(String.format("Event with id=%s was not found.", eventId));
        }
        return eventRepository.getReferenceById(eventId);
    }

    @Transactional
    @Override
    public Event getEventByIdAndInitiatorId(int eventId, int initiatorId) {
        Optional<Event> event = eventRepository.getEventByIdAndInitiatorId(eventId, initiatorId);
        if (event.isEmpty()) {
            throw new NotFoundException(String.format("Event with id=%s was not found.", eventId));
        } else {
            return event.get();
        }
    }

    @Transactional
    @Override
    public List<Event> findEventsByIdIn(Set<Integer> ids) {
        List<Event> events = eventRepository.findEventsByIdIn(ids);
        if (events.size() < ids.size()) {
            Set<Integer> difference = findMissingId(ids, getEventsIds(events));
            throw new NotFoundException(String.format("Event with id=%s was not found.", difference));
        }
        return events;
    }

    @Transactional
    @Override
    public List<EventShortDto> toListEventShortDto(List<Event> events) {
        if (events != null && !events.isEmpty()) {
            HashMap<Integer, Integer> confirmedRequests = getConfirmedRequests(events);
            return events.stream()
                    .map(e -> toEventShortDto(e, confirmedRequests.get(e.getId())))
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    private Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    private Event updateEvent(Event oldEvent, UpdateEventRequest updateEvent) {
        oldEvent.setState(getNewStatusEvent(oldEvent, updateEvent));

        if (updateEvent.getEventDate() != null) {
            checkEventDate(updateEvent.getEventDate(), 2);
            oldEvent.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getAnnotation() != null && !updateEvent.getAnnotation().isBlank()) {
            oldEvent.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getCategory() != null) {
            Category category = categoryService.getCategoryById(updateEvent.getCategory());
            oldEvent.setCategory(category);
        }
        if (updateEvent.getDescription() != null && !updateEvent.getDescription().isBlank()) {
            oldEvent.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getLocation() != null) {
            Location location = saveLocation(updateEvent.getLocation());
            oldEvent.setLocation(location);
        }
        if (updateEvent.getPaid() != null) {
            oldEvent.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            oldEvent.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getRequestModeration() != null) {
            oldEvent.setRequestModeration(updateEvent.getRequestModeration());
        }
        if (updateEvent.getTitle() != null && !updateEvent.getTitle().isBlank()) {
            oldEvent.setTitle(updateEvent.getTitle());
        }
        return oldEvent;
    }

    private EventState getNewStatusEvent(Event oldEvent, UpdateEventRequest updateEvent) {
        StateAction stateAction = toStateAction(updateEvent.getStateAction());

        EventState oldState = oldEvent.getState();
        EventState newState = oldState;
        if ((stateAction == SEND_TO_REVIEW || stateAction == CANCEL_REVIEW) && oldState == PUBLISHED) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "Only pending or canceled events can be changed";
            throw new ConflictException(reason, message);
        } else if (stateAction == SEND_TO_REVIEW) {
            newState = PENDING;
        } else if (stateAction == CANCEL_REVIEW) {
            newState = CANCELED;
        }

        if (stateAction == PUBLISH_EVENT && (oldState == CANCELED || oldState == PUBLISHED)) {
            String reason = "For the requested operation the conditions are not met.";
            String message = String.format("Cannot publish the event because it's not in the right state: %s",
                    oldState);
            throw new ConflictException(reason, message);
        } else if (stateAction == PUBLISH_EVENT && oldState == PENDING) {
            newState = PUBLISHED;
        }
        if (stateAction == REJECT_EVENT && oldState == PUBLISHED) {
            String reason = "For the requested operation the conditions are not met.";
            String message = String.format("Cannot canceled the event because it is in the wrong state: %s",
                    oldState);
            throw new ConflictException(reason, message);
        } else if (stateAction == REJECT_EVENT && oldState == PENDING) {
            newState = CANCELED;
        }
        return newState;
    }

    private void checkEventDate(LocalDateTime eventDate, int hour) {
        LocalDateTime now = LocalDateTime.now();
        if (now.plusHours(hour).isAfter(eventDate)) {
            String reason = "For the requested operation the conditions are not met.";
            String message = String.format("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: %s",
                    eventDate);
            throw new ConflictException(reason, message);
        }
    }

    private HashMap<Integer, Integer> getConfirmedRequests(List<Event> events) {
        HashMap<Integer, Integer> confirmedRequests = new HashMap<>();

        Set<Integer> eventsIds = getEventsIds(events);
        List<ParticipationRequestDto> requests = requestRepository
                .findDtoByEventIdsAndStatus(eventsIds, RequestStatus.CONFIRMED);

        for (int eventId : eventsIds) {
            long eventRequests = requests.stream()
                    .filter(r -> r.getEvent() == eventId)
                    .count();
            confirmedRequests.put(eventId, (int) eventRequests);
        }
        return confirmedRequests;
    }

    private Set<Integer> getEventsIds(List<Event> events) {
        if (events.isEmpty()) {
            return new HashSet<>();
        } else {
            return events.stream()
                    .map(Event::getId)
                    .collect(Collectors.toSet());
        }
    }

    private Optional<Comparator<EventShortDto>> getComparatorEventShortDto(String sort) {
        Optional<Comparator<EventShortDto>> comparator = Optional.empty();
        if (sort != null && !sort.isBlank()) {
            if (toEventSort(sort) == EVENT_DATE) {
                comparator = Optional.of(Comparator.comparing(EventShortDto::getEventDate));
            } else if (toEventSort(sort) == VIEWS) {
                comparator = Optional.of(Comparator.comparing(EventShortDto::getViews));
            }
        }
        return comparator;
    }

    private void addView(List<Event> events) {
        for (Event event : events) {
            event.setViews(event.getViews() + 1);
            logStorageChanges("Save view", event.toString());
            eventRepository.save(event);
        }
    }

    private Set<Integer> findMissingId(Set<Integer> desire, Set<Integer> found) {
        Set<Integer> difference = new HashSet<>();
        if (!found.isEmpty()) {
            for (int id : desire) {
                if (!found.contains(id))
                    difference.add(id);
            }
        } else
            difference = desire;
        return difference;
    }
}