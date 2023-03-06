package ru.practicum.explore.ewm.exception;

import lombok.Data;

@Data
public class ConflictException extends RuntimeException {
    private String reason;

    public ConflictException(String reason, String message) {
        super(message);
        this.reason = reason;
    }
}
