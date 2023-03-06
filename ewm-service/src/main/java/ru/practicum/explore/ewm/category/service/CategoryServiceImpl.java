package ru.practicum.explore.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.ewm.category.Category;
import ru.practicum.explore.ewm.category.CategoryDto;
import ru.practicum.explore.ewm.category.CategoryRepository;
import ru.practicum.explore.ewm.exception.NotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static ru.practicum.explore.ewm.utility.Logger.logStorageChanges;
import static ru.practicum.explore.ewm.category.CategoryMapper.toCategory;
import static ru.practicum.explore.ewm.category.CategoryMapper.toCategoryDto;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository catRepository;

    @Transactional
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        Category category = toCategory(categoryDto);
        Category categoryStorage = catRepository.save(category);
        logStorageChanges("Add category", categoryStorage.toString());
        return toCategoryDto(categoryStorage);
    }

    @Transactional
    @Override
    public void delCategory(int catId) {
        try {
            catRepository.deleteById(catId);
        } catch (EntityNotFoundException e) {
            throw new NotFoundException(String.format("Category with id=%s was not found.", catId));
        }
        logStorageChanges("Delete", String.format("Category with id %s", catId));
    }

    @Transactional
    @Override
    public CategoryDto updateCategory(int catId, CategoryDto categoryDto) {
        Category oldCategory = getCategoryById(catId);
        if (categoryDto.getName() != null && !categoryDto.getName().isBlank()) {
            oldCategory.setName(categoryDto.getName());
        }
        Category categoryStorage = catRepository.save(oldCategory);
        logStorageChanges("Update category", categoryStorage.toString());
        return toCategoryDto(categoryStorage);
    }

    @Transactional
    @Override
    public List<CategoryDto> getCategoriesDto(int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        return catRepository.findAllCategories(pageable);
    }

    @Transactional
    @Override
    public CategoryDto getCategoryDtoById(int catId) {
        return toCategoryDto(getCategoryById(catId));
    }

    @Transactional
    @Override
    public Category getCategoryById(int catId) {
        checkCategoryExists(catId);
        return catRepository.getReferenceById(catId);
    }

    private void checkCategoryExists(int catId) {
        if (!catRepository.existsById(catId)) {
            throw new NotFoundException(String.format("Category with id=%s was not found.", catId));
        }
    }
}
