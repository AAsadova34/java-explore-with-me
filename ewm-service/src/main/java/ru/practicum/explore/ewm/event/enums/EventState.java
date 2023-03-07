package ru.practicum.explore.ewm.event.enums;

import ru.practicum.explore.ewm.exception.ConflictException;

import java.util.Optional;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static EventState toEventState(String strState) {
        return EventState.from(strState)
                .orElseThrow(() -> new ConflictException("For the requested operation the conditions are not met.",
                        String.format("Unknown event state: %s", strState)));
    }

    private static Optional<EventState> from(String strState) {
        for (EventState state : values()) {
            if (state.name().equalsIgnoreCase(strState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }

}
