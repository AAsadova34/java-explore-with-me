package ru.practicum.explore.ewm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.event.dto.EventFullDto;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.stats.client.StatsClient;
import ru.practicum.explore.stats.dto.StatsInnerDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static ru.practicum.explore.ewm.event.EventMapper.FORMATTER;
import static ru.practicum.explore.ewm.utility.Logger.logRequest;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {
    public static final String NAME_SERVICE = "ewm-main-service";

    private final EventService eventService;

    private final StatsClient statsClient;

    @GetMapping("/{id}")
    public EventFullDto getEventByPublic(@PathVariable int id, HttpServletRequest request) {
        logRequest(HttpMethod.GET, String.format("/events/%s", id), "no");

        StatsInnerDto statsInnerDto = new StatsInnerDto(NAME_SERVICE, request.getRequestURL().toString(),
                request.getRemoteAddr(), LocalDateTime.now().format(FORMATTER));
        statsClient.addStats(statsInnerDto);

        return eventService.getEventByPublic(id);
    }

    @GetMapping
    public List<EventShortDto> getEventsByPublic(@RequestParam(required = false) String text,
                                                 @RequestParam(required = false) Integer[] categories,
                                                 @RequestParam(required = false) Boolean paid,
                                                 @RequestParam(required = false) String rangeStart,
                                                 @RequestParam(required = false) String rangeEnd,
                                                 @RequestParam(required = false) Boolean onlyAvailable,
                                                 @RequestParam(required = false) String sort,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size,
                                                 HttpServletRequest request) {
        String url = String.format("/events?text=%s&categories=%s&paid=%s&rangeStart=%s&rangeEnd=%s&onlyAvailable=%s" +
                        "&sort=%s&from=%s&size=%s", text, Arrays.toString(categories), paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size);
        logRequest(HttpMethod.GET, url, "no");

        StatsInnerDto statsInnerDto = new StatsInnerDto(NAME_SERVICE, request.getRequestURL().toString(),
                request.getRemoteAddr(), LocalDateTime.now().format(FORMATTER));
        statsClient.addStats(statsInnerDto);

        return eventService.getEventsByPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                sort, from, size);
    }

}
