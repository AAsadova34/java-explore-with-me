package ru.practicum.explore.ewm.compilation.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.compilation.dto.CompilationDto;
import ru.practicum.explore.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.explore.ewm.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {

    @Transactional
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    @Transactional
    void delCompilation(int compId);

    @Transactional
    CompilationDto updateCompilation(int compId, UpdateCompilationDto compilationDto);

    @Transactional
    List<CompilationDto> getCompilations(Boolean pinned, int from, int size);

    @Transactional
    CompilationDto getCompDtoById(int compId);
}
