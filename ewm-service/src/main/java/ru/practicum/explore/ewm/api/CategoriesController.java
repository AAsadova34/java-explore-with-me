package ru.practicum.explore.ewm.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.ewm.category.CategoryDto;
import ru.practicum.explore.ewm.category.service.CategoryService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import java.util.List;

import static ru.practicum.explore.ewm.utility.Logger.logRequest;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoryService catService;

    @GetMapping
    public List<CategoryDto> getCategoriesDto(@RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                              @RequestParam(defaultValue = "10") @Positive int size) {
        logRequest(HttpMethod.GET, String.format("/categories?from=%s&size=%s", from, size), "no");
        return catService.getCategoriesDto(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategoryDtoById(@PathVariable int catId) {
        logRequest(HttpMethod.GET, String.format("/categories/%s", catId), "no");
        return catService.getCategoryDtoById(catId);
    }

}
