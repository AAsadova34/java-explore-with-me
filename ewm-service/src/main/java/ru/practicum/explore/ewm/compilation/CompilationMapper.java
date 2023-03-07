package ru.practicum.explore.ewm.compilation;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.ewm.compilation.dto.CompilationDto;
import ru.practicum.explore.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.explore.ewm.event.Event;
import ru.practicum.explore.ewm.event.dto.EventShortDto;

import java.util.List;

@UtilityClass
public class CompilationMapper {

    public static Compilation toCompilation(NewCompilationDto compilationDto, List<Event> events) {
        return new Compilation()
                .setEvents(events)
                .setPinned(compilationDto.getPinned())
                .setTitle(compilationDto.getTitle());
    }

    public static CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto()
                .setId(compilation.getId())
                .setEvents(events)
                .setPinned(compilation.getPinned())
                .setTitle(compilation.getTitle());
    }
}
