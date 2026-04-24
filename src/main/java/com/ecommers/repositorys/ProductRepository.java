package com.ecommers.repositorys;

import com.ecommers.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>
{
    Optional<Product> findBySku(String sku);

    Optional<Product> findBySlug(String slug);

    boolean existsBySku(String sku);

    boolean existsBySlug(String slug);

    List<Product> findByActiveTrue();

    List<Product> findByCategory_Id(UUID categoryId);

    List<Product> findByBrand_Id(UUID brandId);

    List<Product> findByNameContainingIgnoreCase(String name);

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Product> findByStockGreaterThan(Integer stock);
}
