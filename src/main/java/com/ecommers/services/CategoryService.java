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
}
