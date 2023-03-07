package ru.practicum.explore.ewm.event.enums;

import ru.practicum.explore.ewm.exception.ConflictException;

import java.util.Optional;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static EventSort toEventSort(String strSort) {


        return EventSort.from(strSort)
                .orElseThrow(() -> new ConflictException("For the requested operation the conditions are not met.",
                        String.format("Unknown event sort: %s", strSort)));
    }

    private static Optional<EventSort> from(String strSort) {
        for (EventSort sort : values()) {
            if (sort.name().equalsIgnoreCase(strSort)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }
}
