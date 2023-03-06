package ru.practicum.explore.ewm.participation_request.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoInc;
import ru.practicum.explore.ewm.participation_request.dto.RequestStatusUpdateDtoOut;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    @Transactional
    List<ParticipationRequestDto> getRequestsByUser(int userId, int eventId);

    @Transactional
    RequestStatusUpdateDtoOut updateRequestStatusByUser(int userId, int eventId,
                                                        RequestStatusUpdateDtoInc updateRequestDtoInc);

    @Transactional
    List<ParticipationRequestDto> getRequestsByUser(int userId);

    @Transactional
    ParticipationRequestDto addRequestByUser(int requesterId, int eventId);

    @Transactional
    ParticipationRequestDto cancelRequestByUser(int requesterId, int requestId);

    @Transactional
    int getNumberRequests(int eventId, RequestStatus status);

}
