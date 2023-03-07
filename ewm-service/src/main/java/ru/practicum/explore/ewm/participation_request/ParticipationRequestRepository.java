package ru.practicum.explore.ewm.participation_request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto;
import ru.practicum.explore.ewm.participation_request.enums.RequestStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RepositoryRestResource
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Integer> {

    ParticipationRequest save(ParticipationRequest participationRequest);

    @Query("SELECT new ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto(pr.id, " +
            "pr.created, pr.event.id, pr.requester.id, pr.status) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.event.id = ?1 AND pr.event.initiator.id = ?2")
    List<ParticipationRequestDto> findDtoByEventIdAndInitiatorId(int eventId, int initiatorId);

    @Query("SELECT new ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto(pr.id, " +
            "pr.created, pr.event.id, pr.requester.id, pr.status) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.requester.id = ?1")
    List<ParticipationRequestDto> findDtoByRequesterId(int requesterId);

    boolean existsByRequesterIdAndEventId(int requesterId, int eventId);

    Optional<ParticipationRequest> getRequestByIdAndRequesterId(int requestId, int requesterId);

    List<ParticipationRequest> findParticipationRequestsByStatusAndEvent_Id(RequestStatus status, int eventId);

    List<ParticipationRequest> findParticipationRequestsByIdIn(Set<Integer> ids);

    @Query("SELECT CAST(COUNT(pr.id) AS integer) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.event.id = ?1 AND pr.status = ?2")
    int getNumberRequests(int eventId, RequestStatus status);

    @Query("SELECT new ru.practicum.explore.ewm.participation_request.dto.ParticipationRequestDto(pr.id, " +
            "pr.created, pr.event.id, pr.requester.id, pr.status) " +
            "FROM ParticipationRequest AS pr " +
            "WHERE pr.event.id IN :eventId AND pr.status = :status")
    List<ParticipationRequestDto> findDtoByEventIdsAndStatus(Set<Integer> eventId, RequestStatus status);

}
