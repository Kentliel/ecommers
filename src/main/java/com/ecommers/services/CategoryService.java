package com.ecommers.services;

import com.ecommers.dtos.CategoryRequestDto;
import com.ecommers.dtos.CategoryResponseDto;
import com.ecommers.models.Category;
import com.ecommers.repositorys.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/* servicio que encapsula la lógica de negocio para manejar categorías
* Usa un categoryRepository para acceder a la persistencia*/
@Service
@RequiredArgsConstructor
public class CategoryService
{
    // Repositorio para operaciones CRUD sobre Category (Inyectado por Spring)
    private final CategoryRepository categoryRepository;

    /*Recupera todas las categorías y las transforma a DTOs de respuesta
    * @return lista de CategoryResponseDto con todas las categorías*/
    public List<CategoryResponseDto> findAll()
    {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponseDto)// convierte cada Category a CategoryResponseDto
                .toList();
    }

    /*Busca una categoria por su id
    * @param id UUID de la categoria a buscar
    * @return CategoryResponseDto con los datos de la categoria encontrada
    * @throws RuntimeException si no existe la categoria*/
    public CategoryResponseDto findById(UUID id)
    {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toResponseDto(category);
    }

    /*Crea una nueva categoria a partir de un DTO de petición
    * @param dto CategoryRequestDto con los datos para crear la categoria
    * @return CategoryResponseDto con la categoria creada (incluye id generado)*/
    public CategoryResponseDto create(CategoryRequestDto dto) {
        Category category = Category.builder()
                .name(dto.getName())
                .slug(dto.getSlug())
                .description(dto.getDescription())
                .active(dto.getActive())
                .build();

        return toResponseDto(categoryRepository.save(category));
    }

    /*Actualiza una categoria existente con los datos del DTO
    * @param id UUID de la categoria a actualizar
    * @param dto CategoryRequestDto con los nuevos valores
    * @return CategoryResponseDto con la categoria actualizada
    * @throws RuntimeException si la categoria no existe*/
    public CategoryResponseDto update(UUID id, CategoryRequestDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(dto.getName());
        category.setSlug(dto.getSlug());
        category.setDescription(dto.getDescription());
        category.setActive(dto.getActive());

        return toResponseDto(categoryRepository.save(category));
    }

    /*Elimina una categoria por su id
    * @param id UUID de la categoria a eliminar*/
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }

    /* Método auxiliar privado que transforma una entidad Category a CategoryResponseDto
    * Centraliza la conversion para evitar duplicación de código
    * @param category entidad a convertir
    * @return DTO de respuesta con los campos relevantes*/
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
