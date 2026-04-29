package com.ecommers.services;

import com.ecommers.dtos.CategoryRequestDto;
import com.ecommers.dtos.CategoryResponseDto;
import com.ecommers.models.Category;
import com.ecommers.repositorys.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService
{
    private final CategoryRepository categoryRepository;

    public List<CategoryResponseDto> findAll()
    {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public CategoryResponseDto findById(UUID id)
    {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toResponseDto(category);
    }

    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .active(dto.getActive())
                .build();

        return toResponseDto(categoryRepository.save(category));
    }

    public CategoryResponseDto update(UUID id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setActive(dto.getActive());

        return toResponseDto(categoryRepository.save(category));
    }

    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponseDto toResponseDto(Category category) {
        return CategoryResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .description(category.getDescription())
                .active(category.getActive())
                .build();
    }
}
