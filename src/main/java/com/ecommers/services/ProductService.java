package com.ecommers.services;

import com.ecommers.dtos.ProductRequestDto;
import com.ecommers.dtos.ProductResponseDto;
import com.ecommers.models.Brand;
import com.ecommers.models.Category;
import com.ecommers.models.Product;
import com.ecommers.repositorys.BrandRepository;
import com.ecommers.repositorys.CategoryRepository;
import com.ecommers.repositorys.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    public List<ProductResponseDto> findAll()
    {
        return productRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public ProductResponseDto findById(UUID id)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return toResponseDto(product);
    }

    public ProductResponseDto create(ProductRequestDto dto)
    {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        Product product = Product.builder()
                .sku(dto.getSku())
                .name(dto.getName())
                .slug(generateSlug(dto)) // se genera el slug basándonos en el nombre
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .imageUrl(dto.getImageUrl())
                .active(dto.getActive())
                .category(category)
                .brand(brand)
                .build();

        return toResponseDto(productRepository.save(product));
    }

    public ProductResponseDto update(UUID id, ProductRequestDto dto)
    {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Brand brand = brandRepository.findById(dto.getBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        product.setSku(dto.getSku());
        product.setName(dto.getName());
        //product.setSlug(product.getSlug());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImageUrl(dto.getImageUrl());
        product.setActive(dto.getActive());
        product.setCategory(category);
        product.setBrand(brand);

        return toResponseDto(productRepository.save(product));
    }

    public void delete(UUID id)
    {
        productRepository.deleteById(id);
    }

    private String generateSlug(ProductRequestDto dto) {
        return dto.getName().replace(" ", "-").toLowerCase();
    }

    private ProductResponseDto toResponseDto(Product product)
    {
        return ProductResponseDto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .active(product.getActive())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .brandId(product.getBrand().getId())
                .brandName(product.getBrand().getName())
                .build();
    }
}
