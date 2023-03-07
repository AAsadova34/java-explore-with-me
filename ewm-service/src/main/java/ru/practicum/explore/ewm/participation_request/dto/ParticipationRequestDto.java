package ru.practicum.explore.ewm.participation_request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ParticipationRequestDto {
    private Integer id;

    private LocalDateTime created;

    private Integer event;

    private Integer requester;

    private RequestStatus status;
}
