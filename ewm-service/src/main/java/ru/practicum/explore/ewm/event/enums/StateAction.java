package ru.practicum.explore.ewm.event.enums;

import ru.practicum.explore.ewm.exception.ConflictException;

import java.util.Optional;

public enum StateAction {
    //for user
    SEND_TO_REVIEW,
    CANCEL_REVIEW,

    //for admin
    PUBLISH_EVENT,
    REJECT_EVENT;

    public static StateAction toStateAction(String strState) {
        return StateAction.from(strState)
                .orElseThrow(() -> new ConflictException("For the requested operation the conditions are not met.",
                        String.format("Unknown state action: %s",
                                strState)));
    }

    private static Optional<StateAction> from(String strState) {
        for (StateAction state : values()) {
            if (state.name().equalsIgnoreCase(strState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}
