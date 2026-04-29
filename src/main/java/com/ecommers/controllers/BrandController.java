package com.ecommers.controllers;

import com.ecommers.dtos.BrandRequestDto;
import com.ecommers.dtos.BrandResponseDto;
import com.ecommers.services.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/brands")
@RequiredArgsConstructor
public class BrandController
{
    private final BrandService brandService;

    @GetMapping
    public List<BrandResponseDto> findAll()
    {
        return brandService.findAll();
    }

    @GetMapping("/{id}")
    public BrandResponseDto findById(@PathVariable UUID id)
    {
        return brandService.findById(id);
    }

    @PostMapping
    public BrandResponseDto create(@RequestBody BrandRequestDto dto)
    {
        return brandService.create(dto);
    }

    @PutMapping("/{id}")
    public BrandResponseDto update(@PathVariable UUID id, @RequestBody BrandRequestDto dto)
    {
        return brandService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        brandService.delete(id);
    }

}
