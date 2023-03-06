package ru.practicum.explore.ewm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.compilation.dto.CompilationDto;
import ru.practicum.explore.ewm.compilation.service.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static ru.practicum.explore.ewm.utility.Logger.logRequest;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsController {

    private final CompilationService compService;

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        logRequest(HttpMethod.GET, String.format("/compilations?pinned=%s&from=%s&size=%s",
                pinned, from, size), "no");
        return compService.getCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompDtoById(@PathVariable int compId) {
        logRequest(HttpMethod.GET, String.format("/compilations/%s", compId), "no");
        return compService.getCompDtoById(compId);
    }
}
