package ru.practicum.explore.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.compilation.Compilation;
import ru.practicum.explore.ewm.compilation.CompilationRepository;
import ru.practicum.explore.ewm.compilation.dto.CompilationDto;
import ru.practicum.explore.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.explore.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.service.EventService;
import ru.practicum.explore.ewm.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.explore.ewm.compilation.CompilationMapper.toCompilation;
import static ru.practicum.explore.ewm.compilation.CompilationMapper.toCompilationDto;
import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;


@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compRepository;
    private final EventService eventService;

    @Transactional
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Integer> eventsIds = newCompilationDto.getEvents();
        List<Event> events = new ArrayList<>();
        if (eventsIds != null && !eventsIds.isEmpty()) {
            events = eventService.findEventsByIdIn(new HashSet<>(eventsIds));
        }

        Compilation compilation = toCompilation(newCompilationDto, events);
        Compilation compStorage = compRepository.save(compilation);
        logStorageChanges("Add compilation", compStorage.toString());

        return toCompilationDto(compStorage, eventService.toListEventShortDto(compStorage.getEvents()));
    }

    @Transactional
    @Override
    public void delCompilation(int compId) {
        try {
            compRepository.deleteById(compId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(String.format("Compilation with id=%s was not found.", compId));
        }
        logStorageChanges("Delete", String.format("Compilation with id %s", compId));
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(int compId, UpdateCompilationDto compilationDto) {
        Compilation oldCompilation = getCompilationById(compId);

        List<Integer> newEventsIds = compilationDto.getEvents();
        if (newEventsIds != null && !newEventsIds.isEmpty()) {
            List<Event> events = eventService.findEventsByIdIn(new HashSet<>(newEventsIds));
            oldCompilation.setEvents(events);
        }
        if (compilationDto.getPinned() != null) {
            oldCompilation.setPinned(compilationDto.getPinned());
        }
        Compilation compStorage = compRepository.save(oldCompilation);
        logStorageChanges("Update compilation", compStorage.toString());

        return toCompilationDto(compStorage, eventService.toListEventShortDto(compStorage.getEvents()));
    }

    @Transactional
    @Override
    public List<CompilationDto> getCompilations(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = compRepository.findAllByPinnedIs(pinned, pageable);
        } else {
            compilations = compRepository.findAllBy(pageable);
        }

        return compilations.stream()
                .map(c -> toCompilationDto(c, eventService.toListEventShortDto(c.getEvents())))
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public CompilationDto getCompDtoById(int compId) {
        Compilation comp = getCompilationById(compId);
        return toCompilationDto(comp, eventService.toListEventShortDto(comp.getEvents()));
    }

    private Compilation getCompilationById(int compId) {
        checkCompilationExists(compId);
        return compRepository.getReferenceById(compId);
    }

    private void checkCompilationExists(int compId) {
        if (!compRepository.existsById(compId)) {
            throw new NotFoundException(String.format("Compilation with id=%s was not found.", compId));
        }
    }
}
