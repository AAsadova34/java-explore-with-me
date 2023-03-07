package ru.practicum.explore.ewm.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CompilationRepository extends JpaRepository<Compilation, Integer> {

    Compilation save(Compilation compilation);

    void deleteById(int compId);

    boolean existsById(int compId);

    List<Compilation> findAllByPinnedIs(boolean pinned, Pageable pageable);

    List<Compilation> findAllBy(Pageable pageable);
}
