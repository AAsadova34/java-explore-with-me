package ru.practicum.explore.stats.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.practicum.explore.stats.dto.StatsOuterDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RepositoryRestResource
public interface StatsRepository extends JpaRepository<Stats, Integer> {

    Stats save(Stats stats);

    @Query("SELECT new ru.practicum.explore.stats.dto.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsOuterDto> findAllUniqueId(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.stats.dto.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.timestamp BETWEEN ?1 AND ?2 " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsOuterDto> findAllNotUniqueId(LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.stats.dto.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri, s.ip " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsOuterDto> findByUriAndUniqueId(Set<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT new ru.practicum.explore.stats.dto.StatsOuterDto(s.app, s.uri, CAST(COUNT(s.ip) AS integer)) " +
            "FROM Stats AS s " +
            "WHERE s.uri IN :uris AND s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<StatsOuterDto> findByUriAndNotUniqueId(Set<String> uris, LocalDateTime start, LocalDateTime end);
}
