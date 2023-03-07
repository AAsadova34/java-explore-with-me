package ru.practicum.explore.ewm.participation_request;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;
import ru.practicum.explore.ewm.user.User;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ParticipationRequestMapper {

    public static ParticipationRequest toParticipationRequest(Event event, User requester, RequestStatus status) {
        return new ParticipationRequest()
                .setEvent(event)
                .setRequester(requester)
                .setStatus(status);
    }

    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest request) {
        return new ParticipationRequestDto(request.getId(),
                request.getCreated(),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus());
    }

    public static List<ParticipationRequestDto> toListRequestDto(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> listRequestDto = new ArrayList<>();
        if (requests != null && !requests.isEmpty()) {
            for (ParticipationRequest request : requests) {
                listRequestDto.add(toParticipationRequestDto(request));
            }
        }
        return listRequestDto;
    }
}
