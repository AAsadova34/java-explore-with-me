package ru.practicum.explore.ewm.participation_request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RequestStatusUpdateDtoInc {
    private List<Integer> requestIds;

    private String status;
}
