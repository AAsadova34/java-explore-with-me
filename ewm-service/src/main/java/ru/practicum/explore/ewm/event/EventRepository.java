package ru.practicum.explore.ewm.event;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.explore.ewm.event.enums.EventState;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface EventRepository extends JpaRepository<Event, Integer>, QuerydslPredicateExecutor {

    Event save(Event event);

    Optional<Event> getEventByIdAndInitiatorId(int eventId, int initiatorId);

    Optional<Event> getEventByIdAndState(int eventId, EventState state);

    List<Event> findEventsByInitiatorId(int initiatorId, Pageable pageable);

    Event getReferenceById(int eventId);

    boolean existsById(int eventId);

    List<Event> findEventsByIdIn(Set<Integer> ids);

}