package ru.practicum.explore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface StatsRepository extends JpaRepository<Stats, Integer> {

    Stats save(Stats stats);

    @Query("SELECT new ru.practicum.explore.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(s.ip)")
    List<StatsOuterDto> findAllUniqueId(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip)")
    List<StatsOuterDto> findAllNotUniqueId(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.uri = ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri, s.ip")
    Optional<StatsOuterDto> findByUriAndUniqueId(String uri, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.uri = ?1 AND s.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY s.app, s.uri")
    Optional<StatsOuterDto> findByUriAndNotUniqueId(String uri, LocalDateTime start, LocalDateTime end);
}
