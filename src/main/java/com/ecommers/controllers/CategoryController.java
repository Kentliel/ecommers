package com.ecommers.controllers;

import com.ecommers.dtos.CategoryRequestDto;
import com.ecommers.dtos.CategoryResponseDto;
import com.ecommers.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController
{
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponseDto> findAll()
    {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public CategoryResponseDto findById(@PathVariable UUID id)
    {
        return categoryService.findById(id);
    }

    @PostMapping
    public CategoryResponseDto create(@RequestBody CategoryRequestDto dto)
    {
        return categoryService.create(dto);
    }

    @PutMapping("/{id}")
    public CategoryResponseDto update(@PathVariable UUID id, @RequestBody CategoryRequestDto dto)
    {
        return categoryService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        categoryService.delete(id);
    }

}
