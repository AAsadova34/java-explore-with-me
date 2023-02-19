package ru.practicum.explore;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.service.StatsService;

import java.util.Arrays;
import java.util.List;

import static ru.practicum.explore.Logger.logRequest;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void addStats(@RequestBody StatsInnerDto statsInnerDto) {
        logRequest(HttpMethod.POST, "/hit", statsInnerDto.toString());
        statsService.addStats(statsInnerDto);
    }

    @GetMapping(path = "/stats")
    public List<StatsOuterDto> getStats(@RequestParam String start,
                                        @RequestParam String end,
                                        @RequestParam(required = false) String[] uris,
                                        @RequestParam(required = false) boolean unique) {
        logRequest(HttpMethod.GET, String.format("/stats?start=%s&end=%s&uris=%s&unique=%b",
                start, end, Arrays.toString(uris), unique), "no");
        return statsService.getStats(start, end, uris, unique);
    }

}

