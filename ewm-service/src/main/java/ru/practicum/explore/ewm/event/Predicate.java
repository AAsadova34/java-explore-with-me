package ru.practicum.explore.ewm.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.event.enums.EventState;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.stream.Collectors.toSet;
import static ru.practicum.explore.ewm.utility.ConverterLocalDateTime.toLocalDateTime;

@UtilityClass
public class Predicate {

    public static Optional<BooleanExpression> getPredicateByUserId(Integer[] users) {
        if (users == null || users.length == 0) {
            return empty();
        } else {
            return Optional.of(QEvent.event.initiator.id.in(Set.of(users)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByStates(String[] states) {
        if (states == null || states.length == 0) {
            return empty();
        } else {
            Set<EventState> statesSearch = Arrays.stream(states)
                    .map(EventState::toEventState)
                    .collect(toSet());
            return Optional.of(QEvent.event.state.in(statesSearch));
        }
    }

    public static Optional<BooleanExpression> getPredicateByCategoryId(Integer[] categories) {
        if (categories == null || categories.length == 0) {
            return empty();
        } else {
            return Optional.of(QEvent.event.category.id.in(Set.of(categories)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByStartForAdmin(String strDateTime) {
        if (strDateTime == null || strDateTime.isBlank()) {
            return empty();
        } else {
            return Optional.of(QEvent.event.eventDate.after(toLocalDateTime(strDateTime)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByStartForPublic(String strDateTime) {
        if (strDateTime == null || strDateTime.isBlank()) {
            return Optional.of(QEvent.event.eventDate.after(LocalDateTime.now()));
        } else {
            return Optional.of(QEvent.event.eventDate.after(toLocalDateTime(strDateTime)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByEnd(String strDateTime) {
        if (strDateTime == null || strDateTime.isBlank()) {
            return empty();
        } else {
            return Optional.of(QEvent.event.eventDate.before(toLocalDateTime(strDateTime)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByText(String text) {
        if (text == null || text.isBlank()) {
            return empty();
        } else {
            return Optional.of(QEvent.event.annotation.containsIgnoreCase(text)
                    .or(QEvent.event.description.containsIgnoreCase(text)));
        }
    }

    public static Optional<BooleanExpression> getPredicateByPaid(Boolean paid) {
        if (paid == null) {
            return empty();
        } else {
            return Optional.of(QEvent.event.paid.eq(paid));
        }
    }
}
