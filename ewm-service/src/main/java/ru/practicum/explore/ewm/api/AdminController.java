package ru.practicum.explore.ewm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.category.service.CategoryService;
import ru.practicum.explore.ewm.category.CategoryDto;
import ru.practicum.explore.ewm.compilation.dto.CompilationDto;
import ru.practicum.explore.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.explore.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.explore.ewm.compilation.service.CompilationService;
import ru.practicum.explore.ewm.event.dto.EventFullDto;
import ru.practicum.explore.ewm.event.dto.UpdateEventRequest;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.ewm.user.dto.UserDto;
import ru.practicum.explore.ewm.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.*;
import static ru.practicum.explore.ewm.utility.Logger.logRequest;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Validated
public class AdminController {

    private final CategoryService catService;

    private final UserService userService;

    private final EventService eventService;

    private final CompilationService compService;

    @PostMapping("/categories")
    @ResponseStatus(CREATED) //201
    public CategoryDto addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        logRequest(HttpMethod.POST, "/admin/categories", categoryDto.toString());
        return catService.addCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(NO_CONTENT) //204
    public void delCategory(@PathVariable int catId) {
        logRequest(HttpMethod.DELETE, String.format("/admin/categories/%s", catId), "no");
        catService.delCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable int catId,
                                      @Valid @RequestBody CategoryDto categoryDto) {
        logRequest(HttpMethod.PATCH, String.format("/admin/categories/%s", catId), categoryDto.toString());
        return catService.updateCategory(catId, categoryDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers(@RequestParam(required = false) Integer[] ids,
                                  @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                  @RequestParam(defaultValue = "10") @Positive int size) {
        logRequest(HttpMethod.GET, String.format("/admin/users?ids=%s&from=%s&size=%s", Arrays.toString(ids),
                from, size), "no");
        return userService.getUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(CREATED) //201
    public UserDto addUser(@Valid @RequestBody UserDto userDto) {
        logRequest(HttpMethod.POST, "/admin/users", userDto.toString());
        return userService.addUser(userDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(NO_CONTENT) //204
    public void deleteUser(@PathVariable int userId) {
        logRequest(HttpMethod.DELETE, String.format("/admin/users/%s", userId), "no");
        userService.deleteUser(userId);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEventByAdmin(@PathVariable int eventId,
                                           @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        logRequest(HttpMethod.PATCH, String.format("/admin/events/%s", eventId),
                updateEventRequest.toString());
        return eventService.updateEventByAdmin(eventId, updateEventRequest);
    }

    @GetMapping("/events")
    public List<EventFullDto> getEventsByAdmin(@RequestParam(required = false) Integer[] users,
                                               @RequestParam(required = false) String[] states,
                                               @RequestParam(required = false) Integer[] categories,
                                               @RequestParam(required = false) String rangeStart,
                                               @RequestParam(required = false) String rangeEnd,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        String url = String.format("/admin/events?users=%s&states=%s&categories=%s&rangeStart=%s" +
                        "&rangeEnd=%s&from=%s&size=%s", Arrays.toString(users), Arrays.toString(states),
                Arrays.toString(categories), rangeStart, rangeEnd, from, size);
        logRequest(HttpMethod.GET, url, "no");
        return eventService.getEventsByAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PostMapping("/compilations")
    @ResponseStatus(CREATED) //201
    public CompilationDto addCompilation(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        logRequest(HttpMethod.POST, "/admin/compilations", newCompilationDto.toString());
        return compService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(NO_CONTENT) //204
    public void delCompilation(@PathVariable int compId) {
        logRequest(HttpMethod.DELETE, String.format("/admin/compilations/%s", compId), "no");
        compService.delCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable int compId,
                                            @RequestBody UpdateCompilationDto compilationDto) {
        logRequest(HttpMethod.PATCH, String.format("/admin/compilations/%s", compId), compilationDto.toString());
        return compService.updateCompilation(compId, compilationDto);
    }
}
