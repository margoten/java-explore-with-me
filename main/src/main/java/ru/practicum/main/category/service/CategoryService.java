package ru.practicum.main.category.service;

import org.springframework.stereotype.Service;
import ru.practicum.main.category.dto.CategoryDto;
import ru.practicum.main.category.dto.NewCategoryDto;
import ru.practicum.main.compilations.dto.CompilationDto;

import java.util.List;

@Service
public interface CategoryService {
    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long catId);

    CategoryDto createCategory(NewCategoryDto categoryDto);

    CategoryDto updateCategory(long catId, NewCategoryDto categoryDto);

    void deleteCategory(long catId);
}
