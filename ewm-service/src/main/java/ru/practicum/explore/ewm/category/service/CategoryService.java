package ru.practicum.explore.ewm.category.service;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.category.Category;
import ru.practicum.explore.ewm.category.CategoryDto;

import java.util.List;

public interface CategoryService {
    @Transactional
    CategoryDto addCategory(CategoryDto categoryDto);

    @Transactional
    void delCategory(int catId);

    @Transactional
    CategoryDto updateCategory(int catId, CategoryDto categoryDto);

    @Transactional
    List<CategoryDto> getCategoriesDto(int from, int size);

    @Transactional
    CategoryDto getCategoryDtoById(int catId);

    @Transactional
    Category getCategoryById(int catId);
}
