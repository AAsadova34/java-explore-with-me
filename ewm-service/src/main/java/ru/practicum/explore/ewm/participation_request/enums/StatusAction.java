package ru.practicum.explore.ewm.participation_request.enums;

import ru.practicum.explore.ewm.exception.ConflictException;

import java.util.Optional;

public enum StatusAction {
    CONFIRMED,
    REJECTED;

    public static StatusAction toStatusAction(String strStatus) {
        return StatusAction.from(strStatus)
                .orElseThrow(() -> new ConflictException("For the requested operation the conditions are not met.",
                        String.format("Unknown status action: %s",
                                strStatus)));
    }

    private static Optional<StatusAction> from(String stringStatus) {
        for (StatusAction statusAction : values()) {
            if (statusAction.name().equalsIgnoreCase(stringStatus)) {
                return Optional.of(statusAction);
            }
        }
        return Optional.empty();
    }
}
