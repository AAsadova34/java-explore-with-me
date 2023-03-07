package ru.practicum.explore.ewm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.comment.dto.CommentDtoOut;
import ru.practicum.explore.ewm.comment.dto.CommentDtoInc;
import ru.practicum.explore.ewm.comment.service.CommentService;
import ru.practicum.explore.ewm.event.dto.EventFullDto;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.ewm.event.dto.EventShortDto;
import ru.practicum.explore.ewm.event.dto.NewEventDto;
import ru.practicum.explore.ewm.event.dto.UpdateEventRequest;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoInc;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoOut;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.participation_request.service.ParticipationRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static ru.practicum.explore.ewm.utility.Logger.logRequest;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final EventService eventService;

    private final ParticipationRequestService requestService;

    private final CommentService commentService;

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable int userId,
                                               @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        logRequest(HttpMethod.GET, String.format("/users/%s/events?from=%s&size=%s", userId,
                from, size), "no");
        return eventService.getEventsByUser(userId, from, size);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(CREATED) //201
    public EventFullDto addEventByUser(@PathVariable int userId,
                                       @Valid @RequestBody NewEventDto newEventDto) {
        logRequest(HttpMethod.POST, String.format("/users/%s/events", userId), newEventDto.toString());
        return eventService.addEventByUser(userId, newEventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable int userId,
                                       @PathVariable int eventId) {
        logRequest(HttpMethod.GET, String.format("/users/%s/events/%s", userId, eventId), "no");
        return eventService.getEventByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventByUser(@PathVariable int userId,
                                          @PathVariable int eventId,
                                          @Valid @RequestBody UpdateEventRequest updateEventRequest) {
        logRequest(HttpMethod.PATCH, String.format("/users/%s/events/%s", userId, eventId),
                updateEventRequest.toString());
        return eventService.updateEventByUser(userId, eventId, updateEventRequest);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable int userId,
                                                           @PathVariable int eventId) {
        logRequest(HttpMethod.GET, String.format("/users/%s/events/%s/requests", userId, eventId), "no");
        return requestService.getRequestsByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestStatusUpdateDtoOut updateRequestStatusByUser(@PathVariable int userId,
                                                               @PathVariable int eventId,
                                                               @RequestBody RequestStatusUpdateDtoInc
                                                                       requestStatusUpdateDtoInc) {
        logRequest(HttpMethod.PATCH, String.format("/users/%s/events/%s/requests", userId, eventId),
                requestStatusUpdateDtoInc.toString());
        return requestService.updateRequestStatusByUser(userId, eventId, requestStatusUpdateDtoInc);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestsByUser(@PathVariable int userId) {
        logRequest(HttpMethod.GET, String.format("/users/%s/requests", userId), "no");
        return requestService.getRequestsByUser(userId);
    }

    @PostMapping("/{userId}/requests")
    @ResponseStatus(CREATED) //201
    public ParticipationRequestDto addRequestByUser(@PathVariable int userId,
                                                    @RequestParam int eventId) {
        logRequest(HttpMethod.POST, String.format("/users/%s/requests?eventId=%s", userId, eventId), "no");
        return requestService.addRequestByUser(userId, eventId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelRequestByUser(@PathVariable int userId,
                                                       @PathVariable int requestId) {
        logRequest(HttpMethod.PATCH, String.format("/users/%s/requests/%s/cancel", userId, requestId), "no");
        return requestService.cancelRequestByUser(userId, requestId);
    }

    @PostMapping("/{userId}/comments")
    @ResponseStatus(CREATED) //201
    public CommentDtoOut addCommentByUser(@PathVariable int userId,
                                          @RequestParam int eventId,
                                          @Valid @RequestBody CommentDtoInc commentDtoInc) {
        logRequest(HttpMethod.POST, String.format("/users/%s/comments?eventId=%s", userId, eventId),
                commentDtoInc.toString());
        return commentService.addCommentByUser(userId, eventId, commentDtoInc);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDtoOut updateCommentByUser(@PathVariable int userId,
                                             @PathVariable int commentId,
                                             @Valid @RequestBody CommentDtoInc commentDtoInc) {
        logRequest(HttpMethod.PATCH, String.format("/users/%s/comments/%s", userId, commentId),
                commentDtoInc.toString());
        return commentService.updateCommentByUser(userId, commentId, commentDtoInc);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(NO_CONTENT) //204
    public void delCommentByUser(@PathVariable int userId,
                                 @PathVariable int commentId) {
        logRequest(HttpMethod.DELETE, String.format("users/%s/comments/%s", userId, commentId), "no");
        commentService.delCommentByUser(userId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentDtoOut> getCommentsByUser(@PathVariable int userId,
                                                 @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                 @RequestParam(defaultValue = "10") @Positive int size) {
        logRequest(HttpMethod.GET, String.format("/users/%s/comments?from=%s&size=%s", userId, from, size), "no");
        return commentService.getCommentsByUser(userId, from, size);
    }
}
