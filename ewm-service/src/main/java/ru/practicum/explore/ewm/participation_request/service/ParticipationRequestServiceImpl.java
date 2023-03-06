package ru.practicum.explore.ewm.participation_request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.enums.EventState;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.ewm.exception.ConflictException;
import ru.practicum.explore.ewm.exception.NotFoundException;
import ru.practicum.explore.ewm.participation_request.ParticipationRequest;
import ru.practicum.explore.ewm.participation_request.ParticipationRequestRepository;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoInc;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoOut;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.participation_request.enums.StatusAction;
import ru.practicum.explore.ewm.user.User;
import ru.practicum.explore.ewm.user.service.UserService;

import java.util.*;

import static ru.practicum.explore.ewm.participation_request.enums.StatusAction.toStatusAction;
import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;
import static ru.practicum.explore.ewm.participation_request.ParticipationRequestMapper.*;
import static ru.practicum.explore.ewm.participation_request.enums.RequestStatus.*;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    private final EventService eventService;

    private final UserService userService;

    @Transactional
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(int initiatorId, int eventId) {
        return requestRepository.findDtoByEventIdAndInitiatorId(eventId, initiatorId);
    }

    @Transactional
    @Override
    public RequestStatusUpdateDtoOut updateRequestStatusByUser(int userId, int eventId,
                                                               RequestStatusUpdateDtoInc updateRequestDto) {
        Event event = eventService.getEventByIdAndInitiatorId(eventId, userId);
        StatusAction status = toStatusAction(updateRequestDto.getStatus());
        RequestStatusUpdateDtoOut result = new RequestStatusUpdateDtoOut();
        List<ParticipationRequest> savedConRequests = new ArrayList<>();
        List<ParticipationRequest> savedRejRequests = new ArrayList<>();

        if (event.getParticipantLimit() != 0
                || event.getRequestModeration()
                || updateRequestDto.getRequestIds().isEmpty()) {

            if (status == StatusAction.CONFIRMED) {
                if (event.getParticipantLimit() <
                        (getNumberRequests(eventId, RequestStatus.CONFIRMED) + updateRequestDto.getRequestIds().size())) {
                    String reason = "For the requested operation the conditions are not met.";
                    String message = "The limit of requests for participation has been reached.";
                    throw new ConflictException(reason, message);
                } else {
                    Set<Integer> confirmedIds = new HashSet<>(updateRequestDto.getRequestIds());
                    List<ParticipationRequest> confirmedRequests = requestRepository
                            .findParticipationRequestsByIdIn(confirmedIds);
                    savedConRequests = saveRequests(updateStatus(confirmedRequests, RequestStatus.CONFIRMED));
                }
                if (event.getParticipantLimit() == getNumberRequests(eventId, RequestStatus.CONFIRMED)) {
                    rejectUnconfirmedRequests(eventId);
                }

            } else if (status == StatusAction.REJECTED) {
                Set<Integer> rejectedIds = new HashSet<>(updateRequestDto.getRequestIds());
                List<ParticipationRequest> rejectedRequests = requestRepository
                        .findParticipationRequestsByIdIn(rejectedIds);
                savedRejRequests = saveRequests(updateStatus(rejectedRequests, RequestStatus.REJECTED));
            }
        }
        result.setConfirmedRequests(toListRequestDto(savedConRequests));
        result.setRejectedRequests(toListRequestDto(savedRejRequests));
        logStorageChanges("Update requests status", result.toString());
        return result;
    }

    @Transactional
    @Override
    public List<ParticipationRequestDto> getRequestsByUser(int userId) {
        return requestRepository.findDtoByRequesterId(userId);
    }

    @Transactional
    @Override
    public ParticipationRequestDto addRequestByUser(int requesterId, int eventId) {
        if (requestRepository.existsByRequesterIdAndEventId(requesterId, eventId)) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "Can't add a repeat request.";
            throw new ConflictException(reason, message);
        }

        Event event = eventService.getEventById(eventId);
        if (requesterId == event.getInitiator().getId()) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "The event initiator cannot add a request to participate in their event.";
            throw new ConflictException(reason, message);
        }
        if (event.getState() != EventState.PUBLISHED) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "You can't participate in an unpublished event.";
            throw new ConflictException(reason, message);
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= getNumberRequests(eventId, CONFIRMED)) {
            String reason = "For the requested operation the conditions are not met.";
            String message = "The limit of requests for participation has been reached.";
            throw new ConflictException(reason, message);
        }

        ParticipationRequest participationRequest;
        User requester = userService.getUserById(requesterId);
        if (event.getRequestModeration()) {
            participationRequest = toParticipationRequest(event, requester, RequestStatus.PENDING);
        } else {
            participationRequest = toParticipationRequest(event, requester, RequestStatus.CONFIRMED);
        }

        ParticipationRequest storageRequest = requestRepository.save(participationRequest);
        logStorageChanges("Add participation request", storageRequest.toString());
        return toParticipationRequestDto(storageRequest);
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequestByUser(int requesterId, int requestId) {
        ParticipationRequest oldRequest = getRequestByIdAndRequesterId(requesterId, requestId);
        oldRequest.setStatus(RequestStatus.CANCELED);

        ParticipationRequest storageRequest = requestRepository.save(oldRequest);
        logStorageChanges("Cancel participation request", storageRequest.toString());
        return toParticipationRequestDto(storageRequest);
    }

    @Transactional
    @Override
    public int getNumberRequests(int eventId, RequestStatus status) {
        return requestRepository.getNumberRequests(eventId, status);
    }

    private List<ParticipationRequest> updateStatus(List<ParticipationRequest> requests, RequestStatus status) {
        List<ParticipationRequest> updateRequests = new ArrayList<>();
        for (ParticipationRequest request : requests) {
            if (request.getStatus() == RequestStatus.PENDING) {
                request.setStatus(status);
                updateRequests.add(request);
            } else {
                String reason = "For the requested operation the conditions are not met.";
                String message = "Request must have status PENDING.";
                throw new ConflictException(reason, message);
            }
        }
        return updateRequests;
    }

    private List<ParticipationRequest> saveRequests(List<ParticipationRequest> requests) {
        List<ParticipationRequest> savedRequests = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            for (ParticipationRequest request : requests) {
                savedRequests.add(requestRepository.save(request));
            }
        }
        return savedRequests;
    }

    private void rejectUnconfirmedRequests(int eventId) {
        List<ParticipationRequest> requests = requestRepository
                .findParticipationRequestsByStatusAndEvent_Id(RequestStatus.PENDING, eventId);
        saveRequests(updateStatus(requests, RequestStatus.REJECTED));
    }

    private ParticipationRequest getRequestByIdAndRequesterId(int requesterId, int requestId) {
        Optional<ParticipationRequest> request = requestRepository.getRequestByIdAndRequesterId(requestId, requesterId);
        if (request.isEmpty()) {
            throw new NotFoundException(String.format("Request with id=%s was not found.", requestId));
        } else {
            return request.get();
        }
    }
}
