package com.ecommers.controllers;

import com.ecommers.dtos.ProductRequestDto;
import com.ecommers.dtos.ProductResponseDto;
import com.ecommers.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController
{
    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDto> findAll()
    {
        return productService.findAll();
    }

    @GetMapping("/{id}")
    public ProductResponseDto findById(@PathVariable UUID id)
    {
        return productService.findById(id);
    }

    @PostMapping
    public ProductResponseDto create(@RequestBody ProductRequestDto dto)
    {
        return productService.create(dto);
    }

    @PutMapping("/{id}")
    public ProductResponseDto update(@PathVariable UUID id, @RequestBody ProductRequestDto dto)
    {
        return productService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id)
    {
        productService.delete(id);
    }

}
