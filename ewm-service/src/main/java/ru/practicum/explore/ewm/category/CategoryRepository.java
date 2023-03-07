package ru.practicum.explore.ewm.category;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category save(Category category);

    void deleteById(int catId);

    boolean existsById(int catId);

    @Query("SELECT new ru.practicum.explore.ewm.category.CategoryDto(c.id, c.name) " +
            "FROM Category AS c")
    List<CategoryDto> findAllCategories(Pageable pageable);

    Category getReferenceById(int catId);
}
