package ru.practicum.explore.ewm.exception;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ErrorResponse {
    private String status;

    private String reason;

    private String message;

    private LocalDateTime timestamp;
}
